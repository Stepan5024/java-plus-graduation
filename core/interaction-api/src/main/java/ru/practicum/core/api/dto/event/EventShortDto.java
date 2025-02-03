package ru.practicum.core.api.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.core.api.constant.Constants;
import ru.practicum.core.api.dto.category.CategoryDto;
import ru.practicum.core.api.dto.location.LocationDto;
import ru.practicum.core.api.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {

    String annotation;

    CategoryDto category;

    Long confirmedRequests;

    @JsonFormat(pattern = Constants.JSON_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime createOn;

    String description;

    @JsonFormat(pattern = Constants.JSON_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;

    Long id;

    UserShortDto initiator;

    LocationDto location;

    boolean paid;

    String title;

    Long views;

    Long likesCount;


}
