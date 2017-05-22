package uk.co.threebugs;

public class WatermarkResult {

    private final Status status;
    private final Ticket ticket;

    private WatermarkResult(Ticket ticket, Status status) {
        this.ticket = ticket;
        this.status = status;
    }

    public static WatermarkResult createAlreadySubmitted(String id) {
        return new WatermarkResult(new Ticket(id), Status.AlreadySubmitted);
    }

    public static WatermarkResult create(Document document) {
        Ticket ticket = new Ticket(document.getAuthor() + document.getTitle());
        return new WatermarkResult(ticket, Status.Submitted);
    }

    public Status getStatus() {
        return status;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
