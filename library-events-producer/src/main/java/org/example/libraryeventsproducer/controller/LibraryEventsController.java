package org.example.libraryeventsproducer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.libraryeventsproducer.domain.LibraryEvent;
import org.example.libraryeventsproducer.producer.LibraryEventProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
public class LibraryEventsController {

    private final LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/async-library-event")
    public ResponseEntity<LibraryEvent> postAsyncLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {
        log.info("Before sendAsyncLibraryEvent");
        libraryEventProducer.sendAsyncLibraryEvent(libraryEvent);
        log.info("After sendAsyncLibraryEvent");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping("/v1/sync-library-event")
    public ResponseEntity<LibraryEvent> postSyncLibraryEvent(@RequestBody LibraryEvent libraryEvent) {
        log.info("Before sendSyncLibraryEvent");
        libraryEventProducer.sendSyncLibraryEvent(libraryEvent);
        log.info("After sendSyncLibraryEvent");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

}
