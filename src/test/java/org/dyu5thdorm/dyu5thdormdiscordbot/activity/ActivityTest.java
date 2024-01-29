package org.dyu5thdorm.dyu5thdormdiscordbot.activity;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.ParticipantsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ActivityTest {
    @Autowired
    ParticipantsRepo repo;

    @Test
    void participant() {
        repo.findAll().forEach(System.out::println);
    }
}
