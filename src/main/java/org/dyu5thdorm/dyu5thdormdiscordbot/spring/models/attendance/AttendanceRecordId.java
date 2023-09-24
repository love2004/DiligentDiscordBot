package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;

@Component
@Data
public class AttendanceRecordId implements Serializable {
    private Student student;
    private LocalDate attendanceDate;
}
