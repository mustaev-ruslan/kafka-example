package org.example.libraryeventsproducer.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.libraryeventsproducer.domain.LibraryEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class LibraryEventProducer {

    private final KafkaTemplate<Integer, LibraryEvent> kafkaTemplate;

    public void sendAsyncLibraryEvent(LibraryEvent libraryEvent) {
        Integer key = libraryEvent.getId();
        ListenableFuture<SendResult<Integer, LibraryEvent>> listenableFuture = kafkaTemplate.sendDefault(key, libraryEvent);

        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(key, libraryEvent, throwable);
            }

            @Override
            public void onSuccess(SendResult<Integer, LibraryEvent> sendResult) {
                handleSuccess(key, libraryEvent, sendResult);
            }
        });
    }

    public void sendSyncLibraryEvent(LibraryEvent libraryEvent) {
        Integer key = libraryEvent.getId();
        ListenableFuture<SendResult<Integer, LibraryEvent>> listenableFuture = kafkaTemplate.sendDefault(key, libraryEvent);

        SendResult<Integer, LibraryEvent> sendResult = null;
        try {
            sendResult = listenableFuture.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            handleFailure(key, libraryEvent, e);
        }
        if (sendResult != null) {
            handleSuccess(key, libraryEvent, sendResult);
        }
    }

    private void handleSuccess(Integer key, LibraryEvent value, SendResult<Integer, LibraryEvent> sendResult) {
        log.info("Message sent successfully. key: {}, value: {}, partition: {}, sendResult: {}",
                key, value, sendResult.getRecordMetadata().partition(), sendResult);
    }

    private void handleFailure(Integer key, LibraryEvent value, Throwable throwable) {
        log.error("Error sending message. key: {}, value: {}", key, value, throwable);
    }


}
