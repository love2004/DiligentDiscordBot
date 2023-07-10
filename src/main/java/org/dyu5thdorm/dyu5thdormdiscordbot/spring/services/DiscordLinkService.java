package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import jakarta.annotation.Nullable;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.DiscordLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiscordLinkService {
    @Autowired
    DiscordLinkRepository discordLinkRepository;
    @Autowired
    DiscordLink discordLink;
    @Autowired
    Student student;

    public boolean isLinked(String discordId) {
        return discordLinkRepository.existsById(discordId);
    }

    public boolean isLinkByStudentId(String studentId) {
        student.setStudentId(studentId);
        return discordLinkRepository.existsByStudent(student);
    }

    public void link(String discordId, String studentId) {
        discordLink.setDiscordId(discordId);
        student.setStudentId(studentId);
        discordLink.setStudent(student);
        discordLinkRepository.save(discordLink);
    }

    public Optional<DiscordLink> findByDiscordId(String discordId) {
        return discordLinkRepository.findById(discordId);
    }

    public void deleteByDiscordId(String discordId) {
        discordLinkRepository.deleteById(discordId);
    }

    public Optional<DiscordLink> findByStudentId(String studentId) {
        return discordLinkRepository.findByStudentStudentId(studentId);
    }
}
