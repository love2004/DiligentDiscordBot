package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
public class ReqLevOperation {
    @Value("${available.time.hour}")
    Integer availableTimeHour;
    @Value("${available.time.minute}")
    Integer availableTimeMinute;

    public boolean isIllegalTime() {
        return isIllegalTime(LocalDateTime.now());
    }

    public boolean isIllegalTime(LocalDateTime time) {
        return time.getHour() >= availableTimeHour && time.getMinute() >= availableTimeMinute;
    }
}
