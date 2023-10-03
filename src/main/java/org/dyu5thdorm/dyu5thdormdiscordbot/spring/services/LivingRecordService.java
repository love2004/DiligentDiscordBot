package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import jakarta.annotation.PostConstruct;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LivingRecordRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class LivingRecordService {
    private final LivingRecordRepo livingRecordRepository;
    @Value("${school_year}")
    private Integer schoolYear;
    @Value("${semester}")
    private Integer semester;
    final
    SchoolTimestamp schoolTimestamp;
    final
    Student student;
    final
    DiscordLinkService discordLinkService;

    public LivingRecordService(SchoolTimestamp schoolTimestamp, LivingRecordRepo livingRecordRepository, Student student, DiscordLinkService discordLinkService) {
        this.schoolTimestamp = schoolTimestamp;
        this.livingRecordRepository = livingRecordRepository;
        this.student = student;
        this.discordLinkService = discordLinkService;
    }

    @PostConstruct
    private void setSchoolTimestamp() {
        schoolTimestamp.setSchoolYear(schoolYear);
        schoolTimestamp.setSemester(semester);
    }

    public Optional<LivingRecord> findByStudentId(String studentId) {
        return livingRecordRepository.findByStudentStudentIdAndSchoolTimestampEquals(studentId, schoolTimestamp);
    }

    public Set<LivingRecord> findAllByStudentIdContains(String studentId) {
        return livingRecordRepository.findAllByStudentStudentIdContainsAndSchoolTimestampEquals(studentId, schoolTimestamp);
    }

    public Set<LivingRecord> findAllByRoomId(String roomId) {
        return livingRecordRepository.findAllByBedBedIdContainsAndSchoolTimestampEquals(roomId, schoolTimestamp);
    }

    public Optional<LivingRecord> findAllByBedId(String bedId) {
        return livingRecordRepository.findByBedBedIdAndAndSchoolTimestampEquals(bedId, schoolTimestamp);
    }

    public Set<LivingRecord> findAllByNameContains(String name) {
        return livingRecordRepository.findAllByStudentNameContainsAndSchoolTimestampEquals(name, schoolTimestamp);
    }

    public LivingRecord findLivingRecordByDiscordId(String discordId) {
        DiscordLink discordLink = discordLinkService.findByDiscordId(discordId);
        if (discordLink == null) return null;
        Optional<LivingRecord> livingRecordOptional = findByStudentId(discordLink.getStudent().getStudentId());
        return livingRecordOptional.orElse(null);
    }

    public Set<LivingRecord> findAllByFloor(Integer floor) {
        return livingRecordRepository.findAllByBedBedIdStartingWith(
                "5" + floor.toString()
        );
    }
}


