package uk.co.threebugs;

import org.springframework.stereotype.Repository;

@Repository
public class WatermarkDataRepository {

    public Watermark lookup(String author, String title) {

        Watermark watermark = new Watermark(
                title,
                author,
                "book",
                "Science");

        return watermark;
    }
}
