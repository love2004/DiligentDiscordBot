package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@Getter
public class ReqLevOperation {
    @Value("${leave.start.time.hour}")
    Integer startLeaveTimeHour;
    @Value("${leave.start.time.min}")
    Integer startLeaveTimeMin;
    @Value("${leave.end.time.hour}")
    Integer endLeaveTimeHour;
    @Value("${leave.end.time.min}")
    Integer endLeaveTimeMin;
    LocalTime startTime;
    LocalTime endTime;

    @PostConstruct
    void init() {
        startTime = LocalTime.of(startLeaveTimeHour, startLeaveTimeMin);
        endTime = LocalTime.of(endLeaveTimeHour, endLeaveTimeMin);
    }

    public boolean isIllegalTime() {
        return isIllegalTime(LocalTime.now());
    }

    public boolean isIllegalTime(@NotNull LocalTime time) {
        return time.isBefore(startTime) || time.isAfter(endTime);
    }
}
