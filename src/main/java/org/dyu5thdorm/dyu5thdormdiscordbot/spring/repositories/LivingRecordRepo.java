package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecordId;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;


public interface LivingRecordRepo extends JpaRepository<LivingRecord, LivingRecordId> {
    Optional<LivingRecord> findByStudentStudentIdAndSchoolTimestampEquals(String student_studentId, SchoolTimestamp schoolTimestamp);
    Set<LivingRecord> findAllByBedBedIdContainsAndSchoolTimestampEquals(String bed_bedId, SchoolTimestamp schoolTimestamp);
    Optional<LivingRecord> findByBedBedIdAndAndSchoolTimestampEquals(String bed_bedId, SchoolTimestamp schoolTimestamp);
    Set<LivingRecord> findAllByStudentStudentIdAndSchoolTimestampEquals(String student_studentId, SchoolTimestamp schoolTimestamp);
    Set<LivingRecord> findAllByStudentStudentIdContainsAndSchoolTimestampEquals(String student_studentId, SchoolTimestamp schoolTimestamp);
    Set<LivingRecord> findAllByStudentNameContainsAndSchoolTimestampEquals(String student_name, SchoolTimestamp schoolTimestamp);
    Set<LivingRecord> findAllByBedBedIdStartingWith(String bed_bedId);
}
