package ru.practicum.core.location.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Table(name = "locations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "lat")
    Float lat;

    @Column(name = "lon")
    Float lon;

    @Transient
    Long likes;

}
