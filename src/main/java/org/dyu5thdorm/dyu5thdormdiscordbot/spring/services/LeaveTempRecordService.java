package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Bed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LeaveTempRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LeaveTempRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeaveTempRecordService {
    final
    LeaveTempRecordRepository repository;
    @Autowired
    LivingRecordService livingRecordService;

    public LeaveTempRecordService(LeaveTempRecordRepository repository) {
        this.repository = repository;

    }

    public boolean isRequested(String bedId, String studentId, LocalDate date) {
        return repository.existsByStudentIdAndDate(bedId, studentId, date);
    }

    public void save(LeaveTempRecord leaveTempRecord) {
        repository.save(leaveTempRecord);
    }

    public void save(Bed bed, Student student, String reason) {
        LeaveTempRecord leaveTempRecord = new LeaveTempRecord();
        leaveTempRecord.setBed(bed);
        leaveTempRecord.setStudent(student);
        leaveTempRecord.setReason(reason);
        repository.save(leaveTempRecord);
    }

    public Set<LeaveTempRecord> findAllByFloorAndDate(Integer floor, LocalDate date) {
        return repository.findAllByRequestTimeDate(date).stream().filter(
                leaveTempRecord -> (leaveTempRecord.getBed().getBedId().charAt(1) - '0') == floor
        ).collect(Collectors.toSet());
    }
}
