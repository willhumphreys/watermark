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

        Mockito.when(watermarkService.submit(Mockito.anyObject())).
                thenReturn(WatermarkResult.createAlreadySubmitted("id"));

        ResponseEntity<?> watermark = watermarkController.watermark(
                new Document("The Dark Code", "Bruce Wayne"));
        assertThat(watermark.getStatusCode(), is(HttpStatus.CONFLICT));
    }
}