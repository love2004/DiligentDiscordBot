package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import java.io.Serializable;

@Entity
@Table(name = "school_timestamp")
@IdClass(SchoolTimestampId.class)
@Data
@NoArgsConstructor
@Component
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