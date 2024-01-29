package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity()
@Builder
@Table(name = "activities")
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Column(name = "activity_description", columnDefinition = "TEXT")
    private String activityDescription;

    @Column(name = "activity_location")
    private String activityLocation;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "registration_deadline", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registrationDeadline;
}
