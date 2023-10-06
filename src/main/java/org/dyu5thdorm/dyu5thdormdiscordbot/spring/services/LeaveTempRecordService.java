package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Bed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LeaveTempRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LeaveTempRecordRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveTempRecordService {
    final
    LeaveTempRecordRepo repository;
    final
    LivingRecordService livingRecordService;

    public LeaveTempRecordService(LeaveTempRecordRepo repository, LivingRecordService livingRecordService) {
        this.repository = repository;

        this.livingRecordService = livingRecordService;
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

    public List<LeaveTempRecord> findAllByFloorAndDate(Integer floor, LocalDate date) {
        return repository.findAllByRequestTimeDate(date).stream().filter(
                leaveTempRecord -> (leaveTempRecord.getBed().getBedId().charAt(1) - '0') == floor
        ).toList();
    }
}
