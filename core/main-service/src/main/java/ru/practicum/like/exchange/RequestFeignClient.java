package ru.practicum.like.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.compilation.model.Status;

@FeignClient(name = "request-service")
public interface RequestFeignClient {
    @GetMapping("/requests/exists")
    boolean existsByEventAndRequesterAndStatus(@RequestParam("eventId") long eventId,
                                               @RequestParam("requesterId") long requesterId,
                                               @RequestParam("status") Status status);
}