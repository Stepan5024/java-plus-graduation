package practicum.endpointHit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.Constants;
import practicum.endpointHit.model.EndpointHit;
import practicum.endpointHit.repository.EndpointHitRepository;
import practicum.exception.WrongDateException;
import practicum.viewStats.model.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Transactional
    @Override
    public void save(EndpointHit endpointHit) {
        log.info("The beginning of the process of creating a statistics record");
        endpointHitRepository.save(endpointHit);
        log.info("The statistics record has been created");
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findByParams(String start, String end, List<String> uris, boolean unique) {
        log.info("The beginning of the process of obtaining statistics of views");

        LocalDateTime decodeStart = decodeTime(start);
        LocalDateTime decodeEnd = decodeTime(end);
        if (decodeStart.isAfter(decodeEnd)) {
            throw new WrongDateException("The start date cannot be after the end date");
        }

        List<ViewStats> listViewStats;

        if (CollectionUtils.isEmpty(uris)) {
            uris = endpointHitRepository.findUniqueUri();
        }

        if (unique) {
            listViewStats = endpointHitRepository.findViewStatsByStartAndEndAndUriAndUniqueIp(decodeStart,
                    decodeEnd,
                    uris);
        } else {
            listViewStats = endpointHitRepository.findViewStatsByStartAndEndAndUri(decodeStart,
                    decodeEnd,
                    uris);
        }

        log.info("Getting the statistics of the views is completed");
        return listViewStats;
    }

    private LocalDateTime decodeTime(String time) {
        String decodeTime = URLDecoder.decode(time, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodeTime, Constants.FORMATTER);
    }
}
