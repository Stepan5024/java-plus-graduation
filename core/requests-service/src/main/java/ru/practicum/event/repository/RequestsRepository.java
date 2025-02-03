package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.core.api.enums.RequestStatus;
import ru.practicum.core.api.enums.Status;
import ru.practicum.event.model.Request;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {

    @Query(value = """
            select EVENT_ID, COUNT(*) as EVENT_COUNT
            from REQUESTS where event_id in (:eventsIds) AND status = :status
            GROUP BY EVENT_ID
            """, nativeQuery = true)
    List<Map<String, Long>> countByStatusAndEventsIds(
            @Param("status") String status, @Param("eventsIds") List<Long> eventsIds);

    List<Request> findAllByRequesterId(long id);

    Optional<Request> findByEventIdAndRequesterId(long eventId, long requesterId);

    long countByStatusAndEventId(RequestStatus status, long eventId);

    List<Request> findAllByStatusAndEventId(Status status, long eventId);

}