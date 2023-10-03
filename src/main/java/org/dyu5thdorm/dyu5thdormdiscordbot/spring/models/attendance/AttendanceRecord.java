package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Bed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Cadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_record")
@IdClass(AttendanceRecordId.class)
@Getter
@Setter
@ToString
@Component
public class AttendanceRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @Id
    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "attendance_date_time")
    private LocalDateTime attendanceDateTime;

    @ManyToOne
    @JoinColumn(name = "bed_id", referencedColumnName = "bed_id")
    private Bed bed;

    @ManyToOne
    @JoinColumn(name = "attendance_status_id", referencedColumnName = "attendance_status_id")
    private AttendanceStatus attendanceStatus;

    @ManyToOne
    @JoinColumn(name = "cadre_id", referencedColumnName = "cadre_id")
    private Cadre cadre;
}
