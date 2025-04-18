package ru.practicum.core.event.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.core.api.dto.location.LocationDto;
import ru.practicum.core.api.dto.user.UserDto;
import ru.practicum.core.api.enums.EventState;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "annotation")
    String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @Transient
    Long confirmedRequests;

    @Column(name = "created_on")
    LocalDateTime createOn;

    @Column(name = "description")
    String description;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "location_id")
    Long locationId;

    @Transient
    LocationDto location;

    @Transient
    Double rating;

    @Column(name = "paid")
    boolean paid;

    @Column(name = "initiator_id")
    Long initiatorId;

    @Transient
    UserDto initiator;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    EventState state;

    @Column(name = "TITLE")
    String title;

    @Transient
    Long views;

    @Transient
    Long likes;

}
