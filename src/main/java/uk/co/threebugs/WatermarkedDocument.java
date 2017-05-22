package uk.co.threebugs;


public class WatermarkedDocument {
    private final Status status;
    private Document document;

    private WatermarkedDocument(Status status) {
        this.status = status;
    }

    public WatermarkedDocument(Document document) {
        this.status = Status.Complete;
        this.document = document;
    }

    public static WatermarkedDocument UnknownTicket() {
        return new WatermarkedDocument(Status.UnknownTicket);
    }

    public static WatermarkedDocument AlreadyProcessing() {
        return new WatermarkedDocument(Status.AlreadyProcessing);
    }

    public Document getDocument() {
        return document;
    }

    public Status getStatus() {
        return status;
    }
}
