package com.example.wordcloudserverworker;

import com.example.wordcloudserverworker.enums.WordCloudStatus;
import com.example.wordcloudserverworker.models.TextFile;
import com.example.wordcloudserverworker.models.Words;
import com.example.wordcloudserverworker.repositories.TextFileRepository;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.ConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MQReceiver {

    @Autowired
    private Logger log;

    @Autowired
    public WordCounter wordCounter;

    @Autowired
    public TextFileRepository textFileRepository;

    @RabbitListener(queues = Constants.QUEUE)
    public void receivedMessage(Message message) throws Exception {
        Long textFileId = message.getMessageProperties().getHeader("TextFileID");
        TextFile textFile = textFileRepository.findById(textFileId).orElse(null);
        if (textFile == null) {
            log.error("TextFile missing in db!");
            return;
        }
        textFile.setStatus(WordCloudStatus.PROGRESS);
        textFileRepository.save(textFile);
        ByteArrayResource resource = new ByteArrayResource(message.getBody());
        MultipartFile file = new CustomMultipartFile(resource);

        float start = System.currentTimeMillis();
        Words words = wordCounter.countWords(file);
        float elapsedTimeInSeconds = (System.currentTimeMillis() - start) / 1000F;
        words.setProcessingTime(elapsedTimeInSeconds);
        log.info("Words count processing time in seconds: " + elapsedTimeInSeconds);

        textFile.setWords(words);
        textFile.setStatus(WordCloudStatus.COMPLETE);
        textFileRepository.save(textFile);
    }

    @Bean
    ConnectionFactoryCustomizer getConnectionFactoryCustomizer() {
        return cf -> cf.setMaxInboundMessageBodySize(1024 * 1024 * 128);
    }
}
