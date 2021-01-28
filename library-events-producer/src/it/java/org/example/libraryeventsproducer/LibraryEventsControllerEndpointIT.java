package org.example.libraryeventsproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.example.libraryeventsproducer.domain.Book;
import org.example.libraryeventsproducer.domain.LibraryEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EmbeddedKafka(topics = "library-events", partitions = 3)
@TestPropertySource(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibraryEventsControllerEndpointIT {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    private Consumer<Integer, LibraryEvent> consumer;

    @BeforeEach
    void setUp() {
        consumer = new DefaultKafkaConsumerFactory<>(
                KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker),
                new IntegerDeserializer(),
                (topic, data) -> sneak(() -> objectMapper.readValue(data, LibraryEvent.class))
        ).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    void postSyncLibraryEvent() {
        // Given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .book(Book.builder().id(1).name("Iliad").author("Homer").build())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, headers);

        // When
        ResponseEntity<LibraryEvent> responseEntity = restTemplate.postForEntity(
                "/v1/sync-library-event", request, LibraryEvent.class);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        LibraryEvent actualValue = KafkaTestUtils.getSingleRecord(consumer, "library-events").value();
        assertEquals(libraryEvent, actualValue);
    }
}
