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


        exService.submit(new FutureTask<>(getAddWatermarkCallable()));

        documentMap = new HashMap<>();
    }

    private Callable<String> getAddWatermarkCallable() {
        return () -> {

            boolean alive = true;

            while (alive) {

                Document documentToWatermark = documentsToWaterMark.take();

                Watermark watermark = new Watermark(
                        "book",
                        documentToWatermark.getTitle(),
                        documentToWatermark.getAuthor(),
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
            return WatermarkResult.createAlreadySubmitted();
        }

        documentMap.put(id, document);


        documentsToWaterMark = new ArrayBlockingQueue<>(100);
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


}
