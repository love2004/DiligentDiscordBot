package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceStatusEnum;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.AttendanceDTO;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Bed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Cadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance.AttendanceRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.attendance.AttendanceStatus;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.AttendanceRecordRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    final
    AttendanceRecordRepo attendanceRecordRepo;

    public void save(AttendanceRecord attendanceRecord) {
        attendanceRecordRepo.save(attendanceRecord);
    }

    public void save(Student student, Bed bed, AttendanceStatusEnum status, Cadre cadre) {
        AttendanceRecord record = new AttendanceRecord();
        record.setStudent(student);
        record.setBed(bed);
        record.setAttendanceStatus(getStatus(status));
        record.setCadre(cadre);
        record.setAttendanceDate(LocalDate.now());
        record.setAttendanceTime(LocalTime.now());
        this.save(record);
    }

    public boolean existByRoomIdAndData(String roomId, LocalDate date) {
        return attendanceRecordRepo.existsByBedBedIdContainsAndAttendanceDateEquals(roomId, date);
    }

    AttendanceStatus getStatus(@NotNull AttendanceStatusEnum status) {
        AttendanceStatus attendanceStatus = new AttendanceStatus();
        attendanceStatus.setAttendanceStatusId(status.getValue());
        return attendanceStatus;
    }

    public Set<AttendanceDTO> findTop5AttendanceRecord(Student student) {
        return this.attendanceRecordRepo.findTop5AttendanceRecordByStudent(student);
    }
}
