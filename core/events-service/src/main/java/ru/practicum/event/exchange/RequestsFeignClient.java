package ru.practicum.event.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.api.dto.request.ParticipationRequestDto;
import ru.practicum.core.api.enums.EventState;

import java.util.List;
import java.util.Set;

@FeignClient(name = "requests-service")
public interface RequestsFeignClient {

    @GetMapping("/requests/event/{eventId}")
    List<ParticipationRequestDto> getRequestsByEventId(@PathVariable long eventId);

    @GetMapping("/requests/confirmed/{eventId}")
    List<ParticipationRequestDto> getConfirmedRequestsByEventId(@PathVariable long eventId);

    @PostMapping("/requests/status")
    void updateRequestsStatus(@RequestBody List<Long> requestIds, @RequestParam EventState status);

    @PostMapping("/requests")
    List<ParticipationRequestDto> getRequestsByIds(@RequestBody Set<Long> requestIds);
}