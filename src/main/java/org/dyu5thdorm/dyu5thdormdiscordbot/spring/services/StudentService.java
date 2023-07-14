package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public boolean exists(String studentId) {
        return studentRepository.existsById(studentId);
    }

    public void saveStudentPhoneNumber(String studentId, String phoneNumber) {
        Optional<Student> getStudent = studentRepository.findById(studentId);
        getStudent.ifPresent(student -> {
            student.setPhoneNumber(phoneNumber);
            studentRepository.save(student);
        });
    }

    public Optional<Student> findByStudentId(String studentId) {
        return studentRepository.findById(studentId);
    }

    public Set<Student> findAllByStudentIdContains(String studentId) {
        return studentRepository.findAllByStudentIdContains(studentId);
    }

    public Set<Student> findAllByStudentId(String studentId) {
        return studentRepository.findAllByStudentId(studentId);
    }
}
