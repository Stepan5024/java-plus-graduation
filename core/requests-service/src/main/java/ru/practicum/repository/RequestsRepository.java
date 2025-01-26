package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.Status;
import ru.practicum.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findByEventId(long eventId);

    List<Request> findByIdIn(Set<Long> id);

    List<Request> findAllByRequesterId(long id);

    Optional<Request> findByEventIdAndRequesterId(long eventId, long requesterId);

    List<Request> findAllByStatusAndEventId(Status status, long eventId);

    boolean existsByEventAndRequesterAndStatus(Event event, User requester, Status status);
}