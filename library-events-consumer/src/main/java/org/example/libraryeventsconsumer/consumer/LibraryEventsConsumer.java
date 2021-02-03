package org.example.libraryeventsconsumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.libraryeventsconsumer.domain.LibraryEvent;
import org.example.libraryeventsconsumer.service.LibraryEventsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryEventsConsumer implements AcknowledgingMessageListener<Integer, LibraryEvent> {

    private final LibraryEventsService libraryEventsService;

    @KafkaListener(topics = "library-events")
    @Override
    public void onMessage(ConsumerRecord<Integer, LibraryEvent> consumerRecord, Acknowledgment acknowledgment) {
        log.info("ConsumerRecord : {}", consumerRecord);
        libraryEventsService.processLibraryEvent(consumerRecord.value());
        acknowledgment.acknowledge();
    }

}
