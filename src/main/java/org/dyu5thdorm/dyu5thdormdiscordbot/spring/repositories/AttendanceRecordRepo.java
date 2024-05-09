package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.AttendanceDTO;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance.AttendanceRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance.AttendanceRecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Set;

public interface AttendanceRecordRepo extends JpaRepository<AttendanceRecord, AttendanceRecordId> {
    boolean existsByBedBedIdContainsAndAttendanceDateEquals(String roomId, LocalDate attendanceDate);
    @Query("SELECT new org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.AttendanceDTO( " +
            "ar.attendanceDate," +
            "ar.attendanceTime," +
            "ar.attendanceStatus.name," +
            "lr.leaveReason)" +
            "from AttendanceRecord ar left join LeaveRecord lr on ar.attendanceDate = lr.leaveDate and ar.student = lr.student " +
            "where ar.student = :student " +
            "order by ar.attendanceDate desc " +
            "limit 10")
    Set<AttendanceDTO> findTop5AttendanceRecordByStudent(Student student);
}