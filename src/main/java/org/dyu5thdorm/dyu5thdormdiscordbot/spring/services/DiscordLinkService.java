package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.DiscordLinkRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscordLinkService {
    final
    DiscordLinkRepo discordLinkRepository;


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

    public Optional<DiscordLink> findByDiscordId(String discordId) {
        return discordLinkRepository.findById(discordId);
    }

    public void deleteByDiscordId(String discordId) {
        discordLinkRepository.deleteById(discordId);
    }

    public DiscordLink findByStudentId(String studentId) {
        return discordLinkRepository.findByStudentStudentId(studentId).orElse(null);
    }
}
