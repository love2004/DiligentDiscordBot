package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoinHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class Dyu5thDormDiscordBotApplicationTests {
    @Autowired
    TookCoinHandler handler;

    @Test
    void contextLoads() throws IOException {
    }
}
