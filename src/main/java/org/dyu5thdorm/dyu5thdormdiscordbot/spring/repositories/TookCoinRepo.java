package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.MachineDTO;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TookCoinRepo extends JpaRepository<TookCoin, Long> {
    boolean existsByEventTimeAndStudent_StudentId(LocalDateTime time, String student_studentId);
    Set<TookCoin> findAllByStudentStudentId(String student_studentId);
    Set<TookCoin> findAllByStudentStudentIdAndGetBackTimeIsNull(String student_studentId);

    @Query("SELECT t FROM TookCoin t WHERE DATE(t.recordTime) < :localDate and t.returnDate is null and t.getBackTime is null")
    Set<TookCoin> findAllByDateAndNotReturn(LocalDate localDate);

    @Query("SELECT t FROM TookCoin t WHERE t.getBackTime is null and t.returnDate is not null")
    Set<TookCoin> findAllNotGetBack();

    @Query("SELECT new org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.MachineDTO(" +
            "t.floor.floor," +
            "t.floor.areaId," +
            "lr.bed.bedId," +
            "t.student.studentId," +
            "t.student.name," +
            "t.machine," +
            "t.description," +
            "t.coinAmount," +
            "t.eventTime," +
            "t.recordTime) " +
            "FROM TookCoin t join LivingRecord  lr on t.student = lr.student " +
            "WHERE lr.schoolTimestamp.schoolYear = :year and lr.schoolTimestamp.semester = :semester and " +
            "t.returnDate is null and t.getBackTime is null and " +
            "t.machine = :machineType")
    List<MachineDTO> findNotGetBackMachine(Integer year, Integer semester, String machineType);
}
