package practicum.endpointHit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import practicum.endpointHit.model.EndpointHit;
import ru.practicum.EndpointHitDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EndpointHitMapper {
    EndpointHit endpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto);

    EndpointHitDto endpointHitToEndpointHitDto(EndpointHit endpointHit);
}
