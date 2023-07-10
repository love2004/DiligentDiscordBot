package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecordId;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.SchoolTimestamp;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;


public interface LivingRecordRepository extends JpaRepository<LivingRecord, LivingRecordId> {
    Optional<LivingRecord> findByStudentStudentId(String studentId);
    Set<LivingRecord> findAllByBedBedIdContainsAndSchoolTimestampEquals(String bed_bedId, SchoolTimestamp schoolTimestamp);
    Set<LivingRecord> findAllByBedBedIdAndSchoolTimestampEquals(String bed_bedId, SchoolTimestamp schoolTimestamp);
}
