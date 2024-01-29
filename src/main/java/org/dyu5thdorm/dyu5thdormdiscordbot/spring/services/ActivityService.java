package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.ActivityRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class ActivityService {
    final
    ActivityRepo activityRepo;

    public boolean overDeadline(Integer actId) {
        return activityRepo.existsByActivityIdAndRegistrationDeadlineBefore(actId, LocalDateTime.now());
    }

    public boolean wasStart(Integer actId) {
        return activityRepo.existsByActivityIdAndStartTimeBefore(actId, LocalDateTime.now());
    }

    public Optional<Activity> findActivity(Integer id) {
        return activityRepo.findById(id);
    }

    public List<Activity> findActivitiesActive() {
        return activityRepo.findAllByRegistrationDeadlineAfter(LocalDateTime.now());
    }

    public List<Activity> findActivitiesByDeadlineTime(LocalDateTime dateTime) {
        return activityRepo.findAllByRegistrationDeadlineAfter(dateTime);
    }
}
