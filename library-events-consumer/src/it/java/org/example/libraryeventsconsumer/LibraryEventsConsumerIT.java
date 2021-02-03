package org.example.libraryeventsconsumer;

import org.example.libraryeventsconsumer.domain.Book;
import org.example.libraryeventsconsumer.domain.LibraryEvent;
import org.example.libraryeventsconsumer.repository.LibraryEventsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(topics = "library-events", partitions = 3)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.template.default-topic=library-events",
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.IntegerSerializer",
        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
})
public class LibraryEventsConsumerIT {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private KafkaListenerEndpointRegistry endpointRegistry;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private KafkaTemplate<Integer, LibraryEvent> kafkaTemplate;

    @Autowired
    private LibraryEventsRepository libraryEventsRepository;

    @BeforeEach
    void setUp() {
        for (MessageListenerContainer listenerContainer : endpointRegistry.getAllListenerContainers()) {
            ContainerTestUtils.waitForAssignment(listenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @AfterEach
    void tearDown() {
        libraryEventsRepository.deleteAll();
    }

    @Test
    @SuppressWarnings("java:S2925")
    void publishNewLibraryEvent() throws InterruptedException {
        // Given
        Book book = Book.builder().id(1).name("Iliad").author("Homer").build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .book(book)
                .build();

        // When
        kafkaTemplate.sendDefault(libraryEvent);
        Thread.sleep(50);

        // Then
        List<LibraryEvent> libraryEvents = libraryEventsRepository.findAll();
        assertEquals(1, libraryEvents.size());
        assertEquals(book, libraryEvents.get(0).getBook());
    }

}
