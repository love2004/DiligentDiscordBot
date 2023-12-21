package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AttendanceRecordId implements Serializable {
    private Student student;
    private LocalDate attendanceDate;
}
