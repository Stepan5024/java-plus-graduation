package practicum.endpointHit.service;

import practicum.endpointHit.model.EndpointHit;
import practicum.viewStats.model.ViewStats;

import java.util.List;

public interface EndpointHitService {
    void save(EndpointHit endpointHit);

    List<ViewStats> findByParams(String start, String end, List<String> uris, boolean unique);
}
