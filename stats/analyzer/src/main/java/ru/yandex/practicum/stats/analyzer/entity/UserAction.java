package ru.yandex.practicum.stats.analyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users_actions")
public class UserAction {

    @Id
    @Column(name = "user_action_id")
    long userActionId;

    @Column(name = "user_id")
    long userId;

    @Column(name = "event_id")
    long eventId;

    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    ActionType actionType;

    @Column(name = "timestamp")
    Instant timestamp;

}
