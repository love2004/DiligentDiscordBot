package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.Getter;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity_participants.ActivityParticipant;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.ActivityPartiRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.ActivityRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityPartiService {
    final
    ActivityPartiRepo activityPartiRepo;
    final
    ActivityRepo activityRepo;

    @Getter
    enum Status {
        PARTICIPATED(1), CANCELED(2), CONFORMED(3);

        private final int value;

        Status(int value) {
            this.value = value;
        }
    }

    public ActivityPartiService(ActivityPartiRepo activityPartiRepo, ActivityRepo activityRepo) {
        this.activityPartiRepo = activityPartiRepo;
        this.activityRepo = activityRepo;
    }

    void save(Student student, Status status, LocalDateTime time) {
        Activity activity = activityRepo.findTopByOrderByActivityIdDesc();
        ActivityParticipant ap = new ActivityParticipant();
        ap.setActivity(activity);
        ap.setStudent(student);
        ap.setParticipationStatus(status.value);
        ap.setRegistrationTime(time);
        this.activityPartiRepo.save(ap);
    }

    public boolean isParticipate(Student student) {
        Activity activity = activityRepo.findTopByOrderByActivityIdDesc();
        return activityPartiRepo.existsByActivityAndStudentAndParticipationStatus(activity, student, Status.PARTICIPATED.value);
    }

    public boolean expired(LocalDateTime time) {
        Activity activity = activityRepo.findTopByOrderByActivityIdDesc();
        return activity.getRegistrationDeadline().isBefore(time);
    }

    public void participate(Student student) {
        save(student, Status.PARTICIPATED, LocalDateTime.now());
    }

    public boolean full() {
        Activity activity = activityRepo.findTopByOrderByActivityIdDesc();
        return activityPartiRepo.countByActivityAndParticipationStatus(activity, 1) >= activity.getParticipantLimit();
    }

    public boolean cancel(Student student) {
        ActivityParticipant participant;
        Activity activity = activityRepo.findTopByOrderByActivityIdDesc();
        if ((participant = activityPartiRepo.findByActivityAndStudent(activity, student)) == null
                || participant.getParticipationStatus() == Status.CANCELED.value) return false;
        participant.setParticipationStatus(Status.CANCELED.value);
        activityPartiRepo.save(participant);
        return true;
    }

    public Integer getParticipantCount() {
        Activity activity = activityRepo.findTopByOrderByActivityIdDesc();
        return activityPartiRepo.countByActivityAndParticipationStatus(activity, 1);
    }
}
