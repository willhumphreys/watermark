package uk.co.threebugs;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Document {
    private String title;
    private String author;
    private Watermark watermark;

    public Document(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Document() {
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void addWatermark(Watermark watermark) {
        this.watermark = watermark;
    }

    @JsonIgnore
    public String getId() {
        return author + title;
    }
}
