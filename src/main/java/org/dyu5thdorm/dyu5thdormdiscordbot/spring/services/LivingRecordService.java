package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import jakarta.annotation.PostConstruct;
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
    SchoolTimestamp schoolTimestamp;
    @Autowired
    Student student;
    @Autowired
    DiscordLinkService discordLinkService;

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

    public Set<LivingRecord> findAllByBedIdContains(String bedId) {
        return livingRecordRepository.findAllByBedBedIdContainsAndSchoolTimestampEquals(bedId, schoolTimestamp);
    }

    public Set<LivingRecord> findAllByBedId(String bedId) {
        return livingRecordRepository.findAllByBedBedIdAndSchoolTimestampEquals(bedId, schoolTimestamp);
    }

    public Set<LivingRecord> findAllByNameContains(String name) {
        return livingRecordRepository.findAllByStudentNameContainsAndSchoolTimestampEquals(name, schoolTimestamp);
    }

    public LivingRecord findLivingRecordByDiscordId(String discordId) {
        Optional<DiscordLink> discordLinkOptional = discordLinkService.findByDiscordId(discordId);
        if (discordLinkOptional.isEmpty()) return null;
        DiscordLink discordLink = discordLinkOptional.get();
        Optional<LivingRecord> livingRecordOptional = findByStudentId(discordLink.getStudent().getStudentId());
        return livingRecordOptional.orElse(null);
    }
}


