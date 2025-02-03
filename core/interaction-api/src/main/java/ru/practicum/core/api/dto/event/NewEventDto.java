package ru.practicum.core.api.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.core.api.constant.Constants;
import ru.practicum.core.api.dto.event.annotation.FutureAfterTwoHours;
import ru.practicum.core.api.dto.location.LocationDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto{

        @NotBlank @Size(min = 20, max = 2000)
        String annotation;

        Long category;

        @NotBlank @Size(min = 20, max = 7000)
        String description;

        @NotNull @FutureAfterTwoHours @JsonFormat(pattern = Constants.JSON_TIME_FORMAT)
        LocalDateTime eventDate;

        @NotNull
        LocationDto location;

        Boolean paid;

        @PositiveOrZero
        Integer participantLimit;

        Boolean requestModeration;

        @NotNull @Size(min = 3, max = 120)
        String title;

}
