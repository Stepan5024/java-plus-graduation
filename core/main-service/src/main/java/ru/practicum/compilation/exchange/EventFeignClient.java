package ru.practicum.compilation.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.compilation.dto.EventDto;

import java.util.List;

@FeignClient(name = "event-service")
public interface EventFeignClient {
    @GetMapping("/events")
    List<EventDto> findAllByIdIn(@RequestParam("ids") List<Long> ids);
}