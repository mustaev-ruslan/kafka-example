package org.example.libraryeventsproducer.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.example.libraryeventsproducer.domain.LibraryEvent;

@SuppressWarnings("unused")
public class LibraryEventSerializer implements Serializer<LibraryEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, LibraryEvent data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialize data", e);
        }
    }
}
