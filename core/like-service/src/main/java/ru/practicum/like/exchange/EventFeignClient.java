package ru.practicum.like.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.like.dto.EventDto;


@FeignClient(name = "event-service")
public interface EventFeignClient {
    @GetMapping("/events/{eventId}")
    EventDto getEventById(@PathVariable long eventId);

    @GetMapping("/events/{eventId}/exists")
    boolean existsEventById(@PathVariable long eventId);

    @PatchMapping("/events/{eventId}/rating")
    void updateEventRating(@PathVariable long eventId, @RequestParam int newRating);
}
