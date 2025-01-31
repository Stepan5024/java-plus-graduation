package practicum.endpointHit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.endpointHit.mapper.EndpointHitMapper;
import practicum.endpointHit.service.EndpointHitService;
import ru.practicum.EndpointHitDto;
import practicum.viewStats.mapper.ViewStatsMapper;
import practicum.viewStats.model.ViewStats;
import ru.practicum.ViewStatsDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @PostMapping("hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Received a POST request to save statistics {}", endpointHitDto);
        endpointHitService.save(endpointHitMapper.endpointHitDtoToEndpointHit(endpointHitDto));
    }

    @GetMapping("stats")
    public List<ViewStatsDto> findByParams(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false) boolean unique) {
        log.info("Received GET request for statistics with parameters start = {}, end = {}, uris = {}, " +
                "unique = {}", start, end, uris, unique);
        List<ViewStats> viewStats = endpointHitService.findByParams(start, end, uris, unique);
        return viewStatsMapper.listViewStatsToListViewStatsDto(viewStats);
    }
}