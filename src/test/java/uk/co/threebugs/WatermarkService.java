package uk.co.threebugs;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

@Service
class WatermarkService {

    private static final int DOCUMENTS_QUEUE_SIZE = 100;
    private static final Logger LOG = getLogger(lookup().lookupClass());
    private final ExecutorService exService;
    private final FutureTask<String> task;
    private Map<String, Document> documentMap;
    private ArrayBlockingQueue<Document> documentsToWaterMark;
    private boolean alive;
    private WatermarkDataRepository watermarkDataRepository;

    @Autowired
    public WatermarkService(WatermarkDataRepository watermarkDataRepository) {

        this.watermarkDataRepository = watermarkDataRepository;

        alive = true;
        exService = Executors.newSingleThreadExecutor();
        documentsToWaterMark = new ArrayBlockingQueue<>(DOCUMENTS_QUEUE_SIZE);
        task = new FutureTask<>(getAddWatermarkCallable());
        exService.submit(task);
        documentMap = new HashMap<>();
    }

    private Callable<String> getAddWatermarkCallable() {
        return () -> {

            while (alive) {

                Thread.sleep(5000);

                Document documentToWatermark = documentsToWaterMark.take();

                Watermark watermark = watermarkDataRepository.lookup(documentToWatermark.getAuthor(),
                        documentToWatermark.getTitle());

                documentToWatermark.addWatermark(watermark);
            }

            return String.format("Shutting Down. Watermarks left on queue: %d",
                    documentsToWaterMark.size());
        };
    }

    public WatermarkResult submit(Document document) {
        String id = document.getId();

        Document existingDocument = documentMap.get(id);

        if (existingDocument != null) {
            return WatermarkResult.createAlreadySubmitted(id);
        }

        documentMap.put(id, document);
        documentsToWaterMark.add(document);

        return WatermarkResult.create(document);
    }

    public WatermarkedDocument get(String ticket) {

        Document document = this.documentMap.get(ticket);

        if (document == null) {
            return WatermarkedDocument.UnknownTicket();
        }

        if (document.getWatermark() == null) {
            return WatermarkedDocument.AlreadyProcessing();
        }

        return new WatermarkedDocument(document);
    }

    public void clear() {
        documentMap.clear();
        documentsToWaterMark.clear();
    }

    @PreDestroy
    public void shutdown() throws InterruptedException, ExecutionException {
        LOG.info("Shutting down");
        alive = false;
        exService.shutdown();

        String taskFinishedMessage = task.get();
        LOG.info("Task finished: " + taskFinishedMessage);

        exService.awaitTermination(1, TimeUnit.MINUTES);
        LOG.info("Watermark service shutdown");
    }
}
