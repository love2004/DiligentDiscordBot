package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance.AttendanceRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance.AttendanceRecordId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AttendanceRecordRepo extends JpaRepository<AttendanceRecord, AttendanceRecordId> {
    boolean existsByBedBedIdContainsAndAttendanceDateEquals(String roomId, LocalDate attendanceDate);
}