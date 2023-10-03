package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscordLinkRepo extends JpaRepository<DiscordLink, String> {
    boolean existsByStudent(Student student);
    Optional<DiscordLink> findByStudentStudentId(String student_studentId);
}
