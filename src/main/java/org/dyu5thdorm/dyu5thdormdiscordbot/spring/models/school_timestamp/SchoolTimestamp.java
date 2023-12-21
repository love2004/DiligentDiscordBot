package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "school_timestamp")
@IdClass(SchoolTimestampId.class)
@Getter
@Setter
@ToString
public class SchoolTimestamp {
    @Id
    @Column(name = "school_year")
    private Integer schoolYear;

    @Id
    @Column(name = "semester")
    private Integer semester;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}