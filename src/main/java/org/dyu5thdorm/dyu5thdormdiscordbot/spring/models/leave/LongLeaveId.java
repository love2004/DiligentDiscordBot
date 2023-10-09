package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave;


import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
@Data
public class LongLeaveId implements Serializable {
    Student student;
    SchoolTimestamp schoolTimestamp;
}
