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
        libraryEventsRepository.save(libraryEvent);
    }

}
