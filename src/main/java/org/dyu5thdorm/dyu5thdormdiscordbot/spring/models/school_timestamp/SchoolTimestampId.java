package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp;

import lombok.Data;

import java.io.Serializable;

@Data
public class SchoolTimestampId implements Serializable {
    private Integer schoolYear;
    private Integer semester;
}
