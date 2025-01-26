package ru.practicum.dto;


import lombok.Data;

@Data
public class EventDto {
    private long id;
    private String title;
    private String description;
    private String state;
    private long participantLimit;
    private boolean requestModeration;
    private long initiatorId;
}