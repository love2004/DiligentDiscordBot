package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "attendance_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class AttendanceStatus {

    @Id
    @Column(name = "attendance_status_id")
    private byte attendanceStatusId;

    @Column(name = "name", length = 1, nullable = false)
    private char name;
}