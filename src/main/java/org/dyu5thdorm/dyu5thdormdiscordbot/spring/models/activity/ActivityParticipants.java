package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity;

import jakarta.persistence.*;
import lombok.*;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity()
@IdClass(ParticipantsId.class)
@Builder
@Table(name = "activity_participants")
@AllArgsConstructor
public class ActivityParticipants {
    @Id
    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id")
    @NotNull
    Activity activity;

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @NotNull
    Student student;

    @ManyToOne
    @JoinColumn(name = "participation_status", referencedColumnName = "status_code", nullable = false)
    @NotNull
    ParticipationStatus participationStatus;

    @Column(name = "registration_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime registrationTime;
}
