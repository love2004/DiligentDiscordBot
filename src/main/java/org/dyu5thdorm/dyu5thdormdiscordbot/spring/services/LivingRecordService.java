package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.*;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LivingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@PropertySource("classpath:discord.properties")
public class LivingRecordService {
    @Autowired
    private LivingRecordRepository livingRecordRepository;
    @Value("${school_year}")
    private Integer schoolYear;
    @Value("${semester}")
    private Integer semester;
    @Autowired
    private LivingRecordId livingRecordId;
    @Autowired
    private Bed bed;
    @Autowired
    SchoolTimestamp schoolTimestamp;
    @Autowired
    Student student;
    @Autowired
    DiscordLinkService discordLinkService;

    private void setPrimaryKey(String bedId) {
        bed.setBedId(bedId);
        setSchoolTimestamp();
        livingRecordId.setBed(bed);
        livingRecordId.setSchoolTimestamp(schoolTimestamp);
    }

    private void setSchoolTimestamp() {
        schoolTimestamp.setSchoolYear(schoolYear);
        schoolTimestamp.setSemester(semester);
    }

    public Optional<LivingRecord> findByStudentId(String studentId) {
        return livingRecordRepository.findByStudentStudentId(studentId);
    }

    public boolean existsByStudentId(String studentId) {
        return findByStudentId(studentId).isPresent();
    }

    public Optional<LivingRecord> findByBedId(String bedId) {
        setPrimaryKey(bedId);
        return livingRecordRepository.findById(livingRecordId);
    }

    public Set<LivingRecord> findAllByBedIdContains(String bedId) {
        setSchoolTimestamp();
        return livingRecordRepository.findAllByBedBedIdContainsAndSchoolTimestampEquals(bedId, schoolTimestamp);
    }

    public Set<LivingRecord> findAllByBedId(String bedId) {
        setSchoolTimestamp();
        return livingRecordRepository.findAllByBedBedIdAndSchoolTimestampEquals(bedId, schoolTimestamp);
    }

    public LivingRecord findLivingRecordByDiscordId(String discordId) {
        Optional<DiscordLink> discordLinkOptional = discordLinkService.findByDiscordId(discordId);
        if (discordLinkOptional.isEmpty()) return null;
        DiscordLink discordLink = discordLinkOptional.get();
        Optional<LivingRecord> livingRecordOptional = findByStudentId(discordLink.getStudent().getStudentId());
        return livingRecordOptional.orElse(null);
    }
}


