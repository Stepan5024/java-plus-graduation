package ru.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "stats-server")
public interface StatFeignClient {
    @PostMapping("/hit")
    EndpointHitDto saveStats(@RequestBody EndpointHitDto hitDto);

    @GetMapping("/stats")
    List<ViewStatsDto> getStats(@RequestParam String start,
                                @RequestParam String end,
                                @RequestParam(required = false) List<String> uris,
                                @RequestParam(required = false) Boolean unique);
}
