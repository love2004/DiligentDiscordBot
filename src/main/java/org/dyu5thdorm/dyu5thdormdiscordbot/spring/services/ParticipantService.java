package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.ActivityParticipants;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.ParticipationStatus;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.ParticipantsRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    final ParticipantsRepo participantsRepo;

    public Integer getParticipantTotal(Activity activity) {
        return this.participantsRepo.countByActivity(activity);
    }

    public Integer getParticipantTotal(Activity activity, ParticipationStatus participationStatus) {
        return this.participantsRepo.countByActivityAndParticipationStatus(activity, participationStatus);
    }

    public Integer getParticipantTotal(Integer actId) {
        return this.participantsRepo.countByActivity_ActivityId(actId);
    }

    public Integer getParticipantTotal(Integer actId, Integer statusCode) {
        return this.participantsRepo.countByActivity_ActivityIdAndParticipationStatus_StatusCode(actId, statusCode);
    }

    public void save(ActivityParticipants activityParticipants) {
        participantsRepo.save(activityParticipants);
    }

    public void save(Integer actId, String studentId, Status statusCode) {
        Activity activity = Activity
                .builder()
                .activityId(actId)
                .build();
        Student student = Student
                .builder()
                .studentId(studentId)
                .build();
        ParticipationStatus participationStatus = ParticipationStatus
                .builder()
                .statusCode(statusCode.getValue())
                .build();
        participantsRepo.save(
                ActivityParticipants
                        .builder()
                        .activity(activity)
                        .student(student)
                        .participationStatus(participationStatus)
                        .registrationTime(LocalDateTime.now())
                        .build()
        );
    }

    @Getter
    public enum Status {
        CONFIRM(1), CANCEL(2), WINNING(3), AGREE(4), DISAGREE(5), ABSTENTION(6);
        private final Integer value;

        Status(Integer value) {
            this.value = value;
        }
    }
}
