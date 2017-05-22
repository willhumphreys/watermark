package uk.co.threebugs;

public class Watermark {
    private String title;
    private String author;
    private String content;
    private String topic;


    public Watermark(String title, String author, String content, String topic) {
        this(title, author, content);
        this.topic = topic;
    }

    public Watermark(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTopic() {
        return topic;
    }
}
