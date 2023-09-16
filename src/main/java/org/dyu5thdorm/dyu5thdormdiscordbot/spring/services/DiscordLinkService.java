package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.DiscordLinkRepository;
import org.springframework.stereotype.Service;

@Service
public class DiscordLinkService {
    final
    DiscordLinkRepository discordLinkRepository;

    public DiscordLinkService(DiscordLinkRepository discordLinkRepository) {
        this.discordLinkRepository = discordLinkRepository;
    }

    public boolean isLinked(String discordId) {
        return discordLinkRepository.existsById(discordId);
    }

    public boolean isLinkByStudentId(String studentId) {
        return discordLinkRepository.findByStudentStudentId(studentId).isPresent();
    }

    public void link(String discordId, String studentId) {
        DiscordLink discordLink = new DiscordLink(discordId);
        Student student = new Student(studentId);
        discordLink.setStudent(student);
        discordLinkRepository.save(discordLink);
    }

    public DiscordLink findByDiscordId(String discordId) {
        return discordLinkRepository.findById(discordId).orElse(null);
    }

    public void deleteByDiscordId(String discordId) {
        discordLinkRepository.deleteById(discordId);
    }

    public DiscordLink findByStudentId(String studentId) {
        return discordLinkRepository.findByStudentStudentId(studentId).orElseGet(null);
    }
}
