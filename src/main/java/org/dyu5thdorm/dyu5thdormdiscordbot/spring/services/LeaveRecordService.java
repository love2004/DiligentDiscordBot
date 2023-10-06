package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Bed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave.LeaveRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LeaveRecordRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class LeaveRecordService {
    final
    LeaveRecordRepo leaveRecordRepo;

    public LeaveRecordService(LeaveRecordRepo leaveRecordRepo) {
        this.leaveRecordRepo = leaveRecordRepo;
    }

    public void save(LeaveRecord leaveRecord) {
        leaveRecordRepo.save(leaveRecord);
    }

    public void save(@NotNull LivingRecord livingRecord, String reason) {
        LeaveRecord leaveRecord = new LeaveRecord();
        leaveRecord.setLeaveReason(reason);
        leaveRecord.setStudent(livingRecord.getStudent());
        leaveRecord.setLeaveDate(LocalDate.now());
        leaveRecord.setLeaveRequestDateTime(LocalDateTime.now());
        leaveRecord.setBed(livingRecord.getBed());
        this.save(leaveRecord);
    }

    public boolean existsByDate(String studentId, String bedId, LocalDate date) {
        return leaveRecordRepo.existsByStudentStudentIdAndBedBedIdAndLeaveDate(studentId, bedId, date);
    }

    public boolean existsByDate(Student student, Bed bed, LocalDate date) {
        return leaveRecordRepo.existsByStudentAndBedAndLeaveDate(student, bed, date);
    }
}
