package ru.yandex.practicum.stats.analyzer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "events_similarities")
public class EventSimilarity {

    @Id
    @Column(name = "event_similarity_id")
    Long eventSimilarityId;

    @Column(name = "event_A_id")
    long eventAId;

    @Column(name = "event_B_id")
    long eventBId;

    @Column(name = "score")
    double score;

    @Column(name = "timestamp")
    Instant timestamp;

}
