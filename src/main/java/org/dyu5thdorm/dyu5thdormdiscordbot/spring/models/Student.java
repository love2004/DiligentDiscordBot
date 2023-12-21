package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString
@NoArgsConstructor
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

