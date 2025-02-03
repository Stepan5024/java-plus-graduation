package ru.practicum.core.api.dto.event;


import lombok.Data;

@Data
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private String state;
    private long participantLimit;
    private boolean requestModeration;
    private Long initiatorId;
    private int rating;
}