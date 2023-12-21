package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class LeaveRecordId implements Serializable {
    private Student student;
    private LocalDate leaveDate;
}
