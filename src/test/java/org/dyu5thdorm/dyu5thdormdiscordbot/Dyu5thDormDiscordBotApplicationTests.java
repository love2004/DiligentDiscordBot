package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Dyu5thDormDiscordBotApplicationTests {
    @Autowired
    TookCoinRepository repository;

    @Test
    void contextLoads() {
        repository.findAll().forEach(
                (e) -> System.out.println(e)
        );
    }

}
