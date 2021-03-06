package uk.co.threebugs;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

@Service
class WatermarkService {

    private static final int DOCUMENTS_QUEUE_SIZE = 100;
    private static final Logger LOG = getLogger(lookup().lookupClass());
    private final ExecutorService exService;
    private final DocumentRepository documentRepository;
    private final ArrayBlockingQueue<Document> documentsToWaterMark;
    private final WatermarkDataRepository watermarkDataRepository;
    private boolean alive;

    @Autowired
    public WatermarkService(WatermarkDataRepository watermarkDataRepository, DocumentRepository documentRepository) {

        this.watermarkDataRepository = watermarkDataRepository;
        this.documentRepository = documentRepository;

        alive = true;
        exService = Executors.newSingleThreadExecutor();
        documentsToWaterMark = new ArrayBlockingQueue<>(DOCUMENTS_QUEUE_SIZE);
        FutureTask<String> task = new FutureTask<>(getAddWatermarkCallable());
        exService.submit(task);
    }

    private Callable<String> getAddWatermarkCallable() {
        return () -> {
            while (alive) {
                Document documentToWatermark = documentsToWaterMark.take();

                Watermark watermark = watermarkDataRepository.lookup(documentToWatermark.getAuthor(),
                        documentToWatermark.getTitle());

                documentToWatermark.addWatermark(watermark);
            }

            LOG.info("Finished pulling documents from the watermark queue");
            return String.format("Shutting Down. Watermarks left on queue: %d",
                    documentsToWaterMark.size());
        };
    }

    public WatermarkResult submit(Document document) {
        String id = document.getId();

        Document existingDocument = documentRepository.get(id);

        if (existingDocument != null) {
            return WatermarkResult.createAlreadySubmitted(id);
        }

        documentRepository.store(document);
        documentsToWaterMark.add(document);

        return WatermarkResult.create(document);
    }

    public WatermarkedDocument get(String ticket) {

        Document document = this.documentRepository.get(ticket);

        if (document == null) {
            return WatermarkedDocument.UnknownTicket();
        }

        if (document.getWatermark() == null) {
            return WatermarkedDocument.AlreadyProcessing();
        }

        return new WatermarkedDocument(document);
    }

    public void clear() {
        this.documentRepository.clear();
        this.documentsToWaterMark.clear();
    }

    @PreDestroy
    public void shutdown() throws InterruptedException, ExecutionException {
        alive = false;
        exService.shutdownNow();
        LOG.info("Watermark service shutdown");
    }
}
