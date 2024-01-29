package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;

import java.io.Serializable;

@Data
public class ParticipantsId implements Serializable {
    Student student;
    Activity activity;
}
