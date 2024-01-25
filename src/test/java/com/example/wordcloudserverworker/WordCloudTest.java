package com.example.wordcloudserverworker;

import com.example.wordcloudserverworker.models.Words;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WordCloudTest {

    private final WordCounter wordCounter = new WordCounter();

    @Test
    public void simpleCountWords() throws Exception {
        // ARRANGE
        // Book: A Room with a View by E. M. Forster
        String url = "https://www.gutenberg.org/cache/epub/2641/pg2641.txt";
        MultipartFile multipartFile = getMultipartFile(url);

        // ACT
        float start = System.currentTimeMillis();
        Words words = wordCounter.countWords(multipartFile);
        float end = System.currentTimeMillis();
        words.setProcessingTime((end - start) / 1000F);

        // ASSERT
        Assertions.assertEquals(69636, words.getCount());
        Assertions.assertEquals("the", words.getItems().getFirst().getText());
        Assertions.assertTrue(2 > words.getProcessingTime(), "Processing time " + words.getProcessingTime() + " is greater than 2 seconds");
    }

    public MultipartFile getMultipartFile(String fileUrl) {
        try {
            URL url = new URI(fileUrl).toURL();
            InputStream in = url.openStream();
            ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());
            return new CustomMultipartFile(resource);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
