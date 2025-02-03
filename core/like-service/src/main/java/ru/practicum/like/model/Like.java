package ru.practicum.like.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "event_id")
    Long eventId;

    @NotNull
    @Column(name = "user_id")
    Long userId;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    StatusLike status;

    @NotNull
    LocalDateTime created;
}
