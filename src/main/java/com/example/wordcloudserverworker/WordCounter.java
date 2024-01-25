package com.example.wordcloudserverworker;

import com.example.wordcloudserverworker.models.Word;
import com.example.wordcloudserverworker.models.Words;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Component
public class WordCounter {

    public Words countWords(MultipartFile file) throws IOException {
        Words words = new Words();
        ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
        int count = 0;
        try (Scanner sc = new Scanner(stream)) {
            while (sc.hasNext()) {
                String wordText = sc.next().replaceAll("[+.!?^:;,]","").toLowerCase();
                Word word = words.getItems().stream().filter(w -> Objects.equals(w.getText(), wordText)).findFirst().orElse(null);
                if (word != null) {
                    word.increaseWordCountByOne();
                } else {
                    word = new Word();
                    word.setText(wordText);
                    word.setValue(1);
                    words.addWordIntoItems(word);
                }
                count++;
            }
        }
        words.setCount(count);
        words.getItems().sort(Comparator.comparingInt(Word::getValue));
        Collections.reverse(words.getItems());
        return words;
    }
}
