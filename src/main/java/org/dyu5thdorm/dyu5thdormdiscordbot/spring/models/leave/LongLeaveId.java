package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave;


import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;

import java.io.Serializable;
@Data
public class LongLeaveId implements Serializable {
    Student student;
    SchoolTimestamp schoolTimestamp;
}
