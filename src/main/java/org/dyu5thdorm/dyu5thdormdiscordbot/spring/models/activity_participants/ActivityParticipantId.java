package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity_participants;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;

import java.io.Serializable;

@Data
public class ActivityParticipantId implements Serializable {
    private Activity activity;
    private Student student;
}
