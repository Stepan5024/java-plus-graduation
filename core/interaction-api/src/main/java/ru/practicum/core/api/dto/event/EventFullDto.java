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
import ru.practicum.core.api.enums.EventState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {


        String annotation;

        CategoryDto category;

        long confirmedRequests;

        @JsonFormat(pattern = Constants.JSON_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
        LocalDateTime createdOn;

        String description;

        @JsonFormat(pattern = Constants.JSON_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
        LocalDateTime eventDate;

        Long id;

        UserShortDto initiator;

        LocationDto location;

        boolean paid;

        Integer participantLimit;

        @JsonFormat(pattern = Constants.JSON_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
        LocalDateTime publishedOn;

        boolean requestModeration;

        EventState state;

        String title;

        Long views;

        long likesCount;


}
