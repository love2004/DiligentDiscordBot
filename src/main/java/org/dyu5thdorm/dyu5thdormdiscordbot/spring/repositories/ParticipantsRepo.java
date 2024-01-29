package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.ActivityParticipants;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.ParticipantsId;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.ParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantsRepo extends JpaRepository<ActivityParticipants, ParticipantsId> {
    Integer countByActivity_ActivityId(Integer activity_activityId);
    Integer countByActivity(Activity activity);
    Integer countByActivityAndParticipationStatus(Activity activity, ParticipationStatus participationStatus);
    Integer countByActivity_ActivityIdAndParticipationStatus_StatusCode(Integer activity_activityId, Integer participationStatus_statusCode);
}
