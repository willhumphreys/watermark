package uk.co.threebugs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WatermarkControllerTest {

    private WatermarkController watermarkController;

    @Mock
    private WatermarkService watermarkService;

    @Before
    public void setUp() throws Exception {
        watermarkController = new WatermarkController(watermarkService);
    }

    @Test
    public void shouldReturnAConflictStatusIfTheDocumentHasAlreadyBeenSubmitted() throws Exception {

        when(watermarkService.submit(Mockito.anyObject())).
                thenReturn(WatermarkResult.createAlreadySubmitted("id"));

        ResponseEntity<?> watermark = watermarkController.watermark(
                new Document("The Dark Code", "Bruce Wayne"));
        assertThat(watermark.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    public void shouldReturnABadRequestStatusIfTheTicketIsUnknown() throws Exception {
        when(watermarkService.get(Mockito.anyString())).thenReturn(WatermarkedDocument.UnknownTicket());

        ResponseEntity<?> notAValidTick = watermarkController.watermark("NotAValidTick");

        assertThat(notAValidTick.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}