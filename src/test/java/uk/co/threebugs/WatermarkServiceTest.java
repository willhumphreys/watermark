package uk.co.threebugs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WatermarkServiceTest {

    private WatermarkService watermarkService;

    @Mock
    private WatermarkDataRepository mockWatermarkDataRepository;

    @Mock
    private DocumentRepository mockDocumentRepository;

    @Before
    public void setUp() throws Exception {
        watermarkService = new WatermarkService(mockWatermarkDataRepository, mockDocumentRepository);
    }

    @Test
    public void shouldReturnAStatusOfAlreadySubmittedIfTheDocumentHasAlreadyBeenSubmitted() throws Exception {
        when(mockDocumentRepository.get(Mockito.anyString())).thenReturn(new Document());

        WatermarkResult result = watermarkService.submit(new Document("The Dark Code", "Bruce Wayne"));
        assertThat(result.getStatus(), is(equalTo(Status.AlreadySubmitted)));
    }
}