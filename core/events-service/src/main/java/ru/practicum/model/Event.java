package ru.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.core.api.dto.location.LocationDto;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.dto.user.UserDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String annotation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @NotNull
    @Column(name = "created_on")
    LocalDateTime createdOn;

    @NotBlank
    String description;

    @NotNull
    @Column(name = "event_date")
    LocalDateTime eventDate;

    @NotNull
    @Column(name = "initiator_id")
    Long initiatorId;

    @Transient
    private UserDto initiator;

    @NotNull
    @Column(name = "location_id")
    private Long locationId;

    @Transient
    private LocationDto location;

    @NotNull
    @Column
    Boolean paid;

    @NotNull
    Long participantLimit;

    @NotNull
    @Column
    LocalDateTime publishedOn;

    @NotNull
    Boolean requestModeration;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    EventState state;

    @NotBlank
    String title;

    @Column(name = "confirmed_requests")
    Long confirmedRequests;

    @Transient
    Long rating;

    @Transient
    Long views;
}
