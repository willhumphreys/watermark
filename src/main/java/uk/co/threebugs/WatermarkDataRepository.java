package uk.co.threebugs;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class WatermarkDataRepository {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final int DUMMY_SLEEP_TIME = 5000;

    private final Map<String, Map<String, Watermark>> watermarks;

    public WatermarkDataRepository() {
        watermarks = new HashMap<>();
    }

    @PostConstruct
    public void addWatermarksOnStartup() {
        addWatermark(new Watermark("The Dark Code", "Bruce Wayne", "book", "Science"));
        addWatermark(new Watermark("How to make money", "Dr. Evil", "book", "Business"));
        addWatermark(new Watermark("Journal of human flight routes", "Clark Kent", "journal"));
    }

    private void addWatermark(Watermark watermark) {
        Map<String, Watermark> authorMap = watermarks.get(watermark.getAuthor());
        if (authorMap == null) {
            authorMap = new HashMap<>();
            this.watermarks.put(watermark.getAuthor(), authorMap);
        }
        authorMap.put(watermark.getTitle(), watermark);
    }

    public Watermark lookup(String author, String title) {

        //Lets to sleep for a few seconds to simulate this method taking a while
        //to watermark the document.
        try {
            Thread.sleep(DUMMY_SLEEP_TIME);
        } catch (InterruptedException e) {
            LOG.error("The watermarking lookup got interrupted.", e);
        }

        boolean isAuthorMissing = !watermarks.containsKey(author);
        boolean isTitleMissing = !watermarks.get(author).containsKey(title);

        //If we haven't got the records in supplied dummy data then generate new dummy data
        // to give the appearance this is a real service.
        if (isAuthorMissing || isTitleMissing) {
            return new Watermark(author, title, "book", "Science");
        }
        return watermarks.get(author).get(title);
    }
}
