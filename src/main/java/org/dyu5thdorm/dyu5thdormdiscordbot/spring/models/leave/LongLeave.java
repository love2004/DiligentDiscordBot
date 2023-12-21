package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "long_leave")
@IdClass(LongLeaveId.class)
@Getter
@Setter
@ToString
public class LongLeave {
    @Id
    @OneToOne
    @JoinColumn(referencedColumnName = "student_id", name = "student_id")
    Student student;

    @Id
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "school_year", referencedColumnName = "school_year"),
            @JoinColumn(name = "semester", referencedColumnName = "semester")
    })
    SchoolTimestamp schoolTimestamp;

    @Column(name = "record_time")
    @CreationTimestamp
    LocalDateTime recordTime;
}
