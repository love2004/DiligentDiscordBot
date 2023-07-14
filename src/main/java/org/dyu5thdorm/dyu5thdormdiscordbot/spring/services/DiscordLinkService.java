package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.DiscordLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiscordLinkService {
    @Autowired
    DiscordLinkRepository discordLinkRepository;


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

    public Optional<DiscordLink> findByStudentId(String studentId) {
        return discordLinkRepository.findByStudentStudentId(studentId);
    }
}
