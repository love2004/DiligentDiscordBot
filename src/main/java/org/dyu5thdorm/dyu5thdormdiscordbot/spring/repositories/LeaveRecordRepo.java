package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Bed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave.LeaveRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave.LeaveRecordId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface
LeaveRecordRepo extends JpaRepository<LeaveRecord, LeaveRecordId> {
    boolean existsByStudentStudentIdAndBedBedIdAndLeaveDate(String student_studentId, String bed_bedId, LocalDate leaveDate);
    boolean existsByStudentAndBedAndLeaveDate(Student student, Bed bed, LocalDate leaveDate);

    boolean existsByBedBedIdAndLeaveDate(String bed_bedId, LocalDate leaveDate);
    Set<LeaveRecord> findByLeaveDate(LocalDate leaveDate);
}
