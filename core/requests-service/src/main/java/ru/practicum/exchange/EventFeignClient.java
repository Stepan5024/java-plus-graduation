package ru.practicum.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.EventDto;

@FeignClient(name = "events-service")
public interface EventFeignClient {

    @GetMapping("/events/{eventId}")
    EventDto getEventById(@PathVariable("eventId") long eventId);

    @GetMapping("/events/{eventId}/initiator/{userId}")
    EventDto getEventByIdAndInitiatorId(@PathVariable("eventId") long eventId, @PathVariable("userId") long userId);
}