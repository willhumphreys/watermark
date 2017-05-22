package uk.co.threebugs;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
class WatermarkService {


    private Map<String, Document> documentMap;
    private ArrayBlockingQueue<Document> documentsToWaterMark;

    public WatermarkService() {

        ExecutorService exService = Executors.newSingleThreadExecutor();

        documentsToWaterMark = new ArrayBlockingQueue<>(100);

        exService.submit(new FutureTask<>(getAddWatermarkCallable()));

        documentMap = new HashMap<>();
    }

    private Callable<String> getAddWatermarkCallable() {
        return () -> {

            boolean alive = true;

            while (alive) {

                Thread.sleep(5000);

                Document documentToWatermark = documentsToWaterMark.take();

                Watermark watermark = new Watermark(
                        documentToWatermark.getTitle(),
                        documentToWatermark.getAuthor(),
                        "book",
                        "Science");

                documentToWatermark.addWatermark(watermark);
            }

            return "hello";
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
}
