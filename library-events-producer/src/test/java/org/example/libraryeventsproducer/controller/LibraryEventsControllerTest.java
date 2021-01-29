package org.example.libraryeventsproducer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.libraryeventsproducer.domain.Book;
import org.example.libraryeventsproducer.domain.LibraryEvent;
import org.example.libraryeventsproducer.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class)
class LibraryEventsControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LibraryEventProducer libraryEventProducer;

    @Test
    void postAsyncLibraryEvent() throws Exception {
        // Given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .book(Book.builder().id(1).name("Iliad").author("Homer").build())
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(post("/v1/async-library-event")
                .content(objectMapper.writeValueAsString(libraryEvent))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    void postAsyncLibraryEvent_badRequest() throws Exception {
        // Given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .book(Book.builder().id(1).name("Iliad").author("").build())
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(post("/v1/async-library-event")
                .content(objectMapper.writeValueAsString(libraryEvent))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string("book.author must not be blank"));
    }

}