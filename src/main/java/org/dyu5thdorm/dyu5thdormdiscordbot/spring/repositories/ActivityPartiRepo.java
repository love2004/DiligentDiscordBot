package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity_participants.ActivityParticipant;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity_participants.ActivityParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityPartiRepo extends JpaRepository<ActivityParticipant, ActivityParticipantId> {
    ActivityParticipant findByActivityAndStudent(Activity activity, Student student);
    boolean existsByActivityAndStudentAndParticipationStatus(Activity activity, Student student, Integer participationStatus);
    int countByActivityAndParticipationStatus(Activity activity, Integer participationStatus);
}
