package uk.co.threebugs;

public class WatermarkResult {

    public static WatermarkResult ALREADY_SUBMITTED = new WatermarkResult(true);

    private boolean alreadySubmitted;
    private Ticket ticket;

    private WatermarkResult(boolean alreadySubmitted) {
        this.alreadySubmitted = alreadySubmitted;
    }

    public WatermarkResult(Ticket ticket) {
        this.ticket = ticket;
    }

    public static WatermarkResult createAlreadySubmitted() {
        return ALREADY_SUBMITTED;
    }

    public static WatermarkResult create(Document document) {
        Ticket ticket = new Ticket(document.getAuthor() + document.getTitle());
        return new WatermarkResult(ticket);
    }

    public boolean isAlreadySubmitted() {
        return alreadySubmitted;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
