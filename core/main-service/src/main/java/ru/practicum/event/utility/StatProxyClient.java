package ru.practicum.event.utility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatFeignClient;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.FORMATTER;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatProxyClient {
    private final StatFeignClient statFeignClient;


    public void saveHit(String app, HttpServletRequest request) {
        log.info("Saving hit for {}", app);
        EndpointHitDto endpointHitDto = toDto(app, request);
        statFeignClient.saveStats(endpointHitDto);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, boolean unique) {
        log.info("Getting stats for {}", uris);
        return statFeignClient.getStats(start.format(FORMATTER), end.format(FORMATTER), uris, unique);
    }

    private EndpointHitDto toDto(String app, HttpServletRequest request) {
        return EndpointHitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
