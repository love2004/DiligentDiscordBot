package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Set;

public interface TookCoinRepository extends JpaRepository<TookCoin, Long> {
    boolean existsByTimeAndStudent_StudentId(LocalDateTime time, String student_studentId);
    Set<TookCoin> findAllByStudentStudentId(String student_studentId);
}
