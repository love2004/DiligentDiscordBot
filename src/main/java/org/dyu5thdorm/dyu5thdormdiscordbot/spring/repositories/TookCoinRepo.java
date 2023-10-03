package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface TookCoinRepo extends JpaRepository<TookCoin, Long> {
    boolean existsByEventTimeAndStudent_StudentId(LocalDateTime time, String student_studentId);
    Set<TookCoin> findAllByStudentStudentId(String student_studentId);
    Set<TookCoin> findAllByStudentStudentIdAndGetBackTimeIsNull(String student_studentId);

    @Query("SELECT t FROM TookCoin t WHERE FUNCTION('DATE', t.recordTime) < :localDate")
    Set<TookCoin> findAllByDateAndNotReturn(LocalDate localDate);

    @Query("SELECT t FROM TookCoin t WHERE t.getBackTime is null and t.returnTime is not null")
    Set<TookCoin> findAllNotGetBack();
}
