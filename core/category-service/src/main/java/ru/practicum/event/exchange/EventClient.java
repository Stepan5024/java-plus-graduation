package ru.practicum.event.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "events-service")
public interface EventClient {
    @GetMapping("/events/category/{categoryId}/exists")
    boolean isCategoryUsed(@PathVariable("categoryId") long categoryId);
}
