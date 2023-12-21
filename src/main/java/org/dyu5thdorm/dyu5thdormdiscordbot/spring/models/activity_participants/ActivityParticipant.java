package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity_participants;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "activity_participants")
@IdClass(ActivityParticipantId.class)
public class ActivityParticipant {

    @Id
    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id", nullable = false)
    private Activity activity;

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @Column(name = "participation_status", nullable = false)
    private Integer participationStatus;

    @Column(name = "registration_time", nullable = false)
    private LocalDateTime registrationTime;
}