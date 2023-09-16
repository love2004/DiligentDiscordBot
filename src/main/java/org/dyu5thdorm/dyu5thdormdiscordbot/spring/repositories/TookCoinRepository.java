package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface TookCoinRepository extends JpaRepository<TookCoin, Long> {
    boolean existsByTimeAndStudent_StudentId(LocalDateTime time, String student_studentId);
    Set<TookCoin> findAllByStudentStudentId(String student_studentId);
    Set<TookCoin> findAllByStudentStudentIdAndIsGetBack(String student_studentId, Boolean isGetBack);

    @Query("SELECT t FROM TookCoin t WHERE t.isReturn = false AND FUNCTION('DATE', t.recordTime) < :localDate")
    Set<TookCoin> findAllByDateAndNotReturn(LocalDate localDate);

    Set<TookCoin> findAllByIsGetBackAndIsReturn(Boolean isGetBack, Boolean isReturn);
}
