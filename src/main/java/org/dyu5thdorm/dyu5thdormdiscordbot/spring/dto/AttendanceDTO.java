package org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AttendanceDTO {
    @NotNull
    LocalDate date;
    @NotNull
    LocalTime time;
    @NotNull
    String attStatus;
    String leaveReason;
}
