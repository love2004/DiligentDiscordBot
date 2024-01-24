package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoinHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;

@SpringBootTest
class Dyu5thDormDiscordBotApplicationTests {
    @Autowired
    TookCoinRepo repo;
    @Autowired
    TookCoinHandler handler;

    @Test
    void contextLoads() throws FileNotFoundException {
    }
}
