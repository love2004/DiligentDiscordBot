package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.Getter;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.ActivityRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Service
public class ActivityService {
    final
    ActivityRepo activityRepo;

    public ActivityService(ActivityRepo activityRepo) {
        this.activityRepo = activityRepo;
    }

    public List<Activity> findActivitiesActive() {
        return activityRepo.findAllByRegistrationDeadlineAfter(LocalDateTime.now());
    }

    public List<Activity> findActivitiesByDeadlineTime(LocalDateTime dateTime) {
        return activityRepo.findAllByRegistrationDeadlineAfter(dateTime);
    }
}
