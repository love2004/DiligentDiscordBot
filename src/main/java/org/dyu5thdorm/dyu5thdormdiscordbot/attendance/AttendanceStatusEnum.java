package org.dyu5thdorm.dyu5thdormdiscordbot.attendance;

import lombok.Getter;

@Getter
public enum AttendanceStatusEnum {
    IN(1), OUT(2), EMPTY(3);
    private final int value;

    AttendanceStatusEnum(int value) {
        this.value = value;
    }
}
