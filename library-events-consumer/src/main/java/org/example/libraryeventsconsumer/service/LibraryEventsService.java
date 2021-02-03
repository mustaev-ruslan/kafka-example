package org.example.libraryeventsconsumer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.libraryeventsconsumer.domain.LibraryEvent;
import org.example.libraryeventsconsumer.repository.LibraryEventsRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryEventsService {

    private final LibraryEventsRepository libraryEventsRepository;

    public void processLibraryEvent(LibraryEvent libraryEvent) {
        log.info("Try process");
        switch (libraryEvent.getBook().getId()) {
            case 400 -> throw new IllegalArgumentException("Illegal argument id 400");
            case 401 -> throw new IllegalAccessError("Illegal access id 401");
            case 500 -> throw new IllegalStateException("Illegal app state for id 500");
            default -> libraryEventsRepository.save(libraryEvent);
        }
    }

}
