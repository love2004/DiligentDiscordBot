package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "attendance_status")
@Getter
@Setter
@ToString
public class AttendanceStatus {

    @Id
    @Column(name = "attendance_status_id")
    private Integer attendanceStatusId;

    @Column(name = "name", length = 1, nullable = false)
    private String name;
}