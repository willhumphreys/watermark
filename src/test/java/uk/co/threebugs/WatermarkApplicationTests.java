package uk.co.threebugs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WatermarkApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnAStatusCodeOfAcceptedWhenTicketInitiallyChecked() {

        Document document = new Document("The Dark Code", "Bruce Wayne");
        HttpEntity<Document> request = new HttpEntity<>(document);
        Ticket ticket = this.restTemplate.postForObject("/api/watermark", request,
                Ticket.class);

        String id = ticket.getId();

        ResponseEntity<Document> waterMarkedDocumentEntity =
                this.restTemplate.getForEntity("/api/watermark?ticket=" + id,
                        Document.class);

        assertThat(waterMarkedDocumentEntity.getStatusCode(), is(equalTo(HttpStatus.ACCEPTED)));

    }

    @Test
    public void shouldReturnWatermarkedBookAfterProcessing() {

        Document document = new Document("The Dark Code", "Bruce Wayne");
        HttpEntity<Document> request = new HttpEntity<>(document);
        Ticket ticket = this.restTemplate.postForObject("/api/watermark", request,
                Ticket.class);

        String id = ticket.getId();

        ResponseEntity<Document> waterMarkedDocumentEntity = getWatermarkedDocumentEntity(id);


        assertThat(waterMarkedDocumentEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));

        Document watermarkedDocument = waterMarkedDocumentEntity.getBody();

        assertThat(watermarkedDocument.getTitle(), is(equalTo("The Dark Code")));
        assertThat(watermarkedDocument.getAuthor(), is(equalTo("Bruce Wayne")));

        Watermark watermark = watermarkedDocument.getWatermark();

        assertThat(watermark.getTitle(), is(equalTo("The Dark Code")));
        assertThat(watermark.getAuthor(), is(equalTo("Bruce Wayne")));
        assertThat(watermark.getContent(), is(equalTo("book")));
        assertThat(watermark.getTopic(), is(equalTo("Science")));

    }

    @Test
    public void shouldReturnWatermarkedJournalAfterProcessing() {

        Document document = new Document("Journal of human flight routes", "Clark Kent");
        HttpEntity<Document> request = new HttpEntity<>(document);
        Ticket ticket = this.restTemplate.postForObject("/api/watermark", request,
                Ticket.class);

        String id = ticket.getId();

        ResponseEntity<Document> waterMarkedDocumentEntity = getWatermarkedDocumentEntity(id);


        assertThat(waterMarkedDocumentEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));

        Document watermarkedDocument = waterMarkedDocumentEntity.getBody();

        assertThat(watermarkedDocument.getTitle(), is(equalTo("Journal of human flight routes")));
        assertThat(watermarkedDocument.getAuthor(), is(equalTo("Clark Kent")));

        Watermark watermark = watermarkedDocument.getWatermark();

        assertThat(watermark.getTitle(), is(equalTo("Journal of human flight routes")));
        assertThat(watermark.getAuthor(), is(equalTo("Clark Kent")));
        assertThat(watermark.getContent(), is(equalTo("journal")));
        assertThat(watermark.getTopic(), is(nullValue()));

    }

    private ResponseEntity<Document> getWatermarkedDocumentEntity(String id) {
        ResponseEntity<Document> waterMarkedDocumentEntity;
        do {
            waterMarkedDocumentEntity =
                    this.restTemplate.getForEntity("/api/watermark?ticket=" + id,
                            Document.class);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Interrupted while sleeping");
            }
        } while (waterMarkedDocumentEntity.getStatusCode() == HttpStatus.ACCEPTED);
        return waterMarkedDocumentEntity;
    }


}
