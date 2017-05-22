package uk.co.threebugs;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class WatermarkDataRepository {

    private Map<String, Map<String, Watermark>> watermarks;

    public WatermarkDataRepository() {
        watermarks = new HashMap<>();
    }

    @PostConstruct
    public void addWatermarksOnStartup() {
        addWatermark(new Watermark("The Dark Code", "Bruce Wayne", "book", "Science"));
        addWatermark(new Watermark("How to make money", "Dr. Evil", "book", "Business"));
        addWatermark(new Watermark("Journal of human flight routes", "Clark Kent", "journal"));
    }

    public void addWatermark(Watermark watermark) {
        Map<String, Watermark> authorMap = watermarks.get(watermark.getAuthor());
        if (authorMap == null) {
            authorMap = new HashMap<>();
            this.watermarks.put(watermark.getAuthor(), authorMap);
        }
        authorMap.put(watermark.getTitle(), watermark);
    }

    public Watermark lookup(String author, String title) {
        boolean isAuthorMissing = !watermarks.containsKey(author);
        boolean isTitleMissing = !watermarks.get(author).containsKey(title);

        //If we haven't got the records in dummy data then generate new dummy data to
        //give the appearance this is a real service.
        if (isAuthorMissing || isTitleMissing) {
            return new Watermark(author, title, "book", "Science");
        }
        return watermarks.get(author).get(title);
    }
}
