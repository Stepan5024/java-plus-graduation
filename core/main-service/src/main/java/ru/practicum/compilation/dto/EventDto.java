package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDto {
    Long id;
    String annotation;
    Long categoryId;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    Long initiatorId;
    Long locationId;
    Boolean paid;
    Long participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    String state; // Если State — это Enum, лучше передать его в виде строки
    String title;
    Long confirmedRequests;
    Long rating;
    Long views;
}