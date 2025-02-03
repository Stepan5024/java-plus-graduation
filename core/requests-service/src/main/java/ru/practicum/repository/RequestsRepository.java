package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.core.api.enums.Status;
import ru.practicum.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(long id);

    Optional<Request> findByEventIdAndRequesterId(long eventId, long requesterId);

    List<Request> findAllByStatusAndEventId(Status status, long eventId);

   }