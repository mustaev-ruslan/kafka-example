package org.example.libraryeventsconsumer.repository;

import org.example.libraryeventsconsumer.domain.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryEventsRepository extends JpaRepository<LibraryEvent, Integer> {
}
