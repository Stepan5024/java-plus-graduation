package ru.practicum.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "requests-service")
public interface RequestsFeignClient {

    @GetMapping("/requests/event/{eventId}")
    List<Request> getRequestsByEventId(@PathVariable long eventId);

    @GetMapping("/requests/confirmed/{eventId}")
    List<Request> getConfirmedRequestsByEventId(@PathVariable long eventId);

    @PostMapping("/requests/status")
    void updateRequestsStatus(@RequestBody List<Long> requestIds, @RequestParam Status status);

    @PostMapping("/requests")
    List<Request> getRequestsByIds(@RequestBody Set<Long> requestIds);
}