package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@Component
public class Student implements Serializable {
    @Id
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "citizenship", nullable = false)
    private String citizenship;

    @Column(name = "phone_number")
    private String phoneNumber;

    public Student(String studentId) {
        this.studentId = studentId;
    }
}

