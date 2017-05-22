package uk.co.threebugs;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WatermarkDataRepositoryTest {

    private WatermarkDataRepository watermarkDataRepository;

    @Before
    public void setUp() throws Exception {
        watermarkDataRepository = new WatermarkDataRepository();
    }

    @Test
    public void shouldUseDummyGeneratedDataIfThereIsNotWatermarkMatch() throws Exception {

        Watermark dummyWatermark = watermarkDataRepository.lookup("Bruce Wayne", "Not Stored");

        assertThat(dummyWatermark.getAuthor(), is(equalTo("Bruce Wayne")));
        assertThat(dummyWatermark.getContent(), is(equalTo("book")));
        assertThat(dummyWatermark.getTopic(), is(equalTo("Science")));
        assertThat(dummyWatermark.getTitle(), is(equalTo("Not Stored")));
    }
}