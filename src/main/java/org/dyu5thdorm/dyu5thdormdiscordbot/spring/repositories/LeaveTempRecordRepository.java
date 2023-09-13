package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LeaveTempRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Set;

public interface LeaveTempRecordRepository extends JpaRepository<LeaveTempRecord, Integer> {
    @Query("SELECT case when count(l) > 0 then true else false end from LeaveTempRecord l WHERE l.student.studentId = :studentId AND FUNCTION('DATE', l.requestTime) = :requestTimeDate AND l.bed.bedId = :bedId")
    boolean existsByStudentIdAndDate(String bedId, String studentId, LocalDate requestTimeDate);
    @Query("SELECT l from LeaveTempRecord l WHERE FUNCTION('DATE', l.requestTime) = :requestTimeDate order by l.bed.bedId")
    Set<LeaveTempRecord> findAllByRequestTimeDate(LocalDate requestTimeDate);
}
