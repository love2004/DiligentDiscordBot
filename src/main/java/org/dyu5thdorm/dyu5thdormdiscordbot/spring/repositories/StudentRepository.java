package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, String> {
    Set<Student> findAllByStudentId(String studentId);
    Set<Student> findAllByStudentIdContains(String studentId);
}
