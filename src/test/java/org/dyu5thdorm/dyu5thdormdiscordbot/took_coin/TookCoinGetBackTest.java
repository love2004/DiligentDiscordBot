package org.dyu5thdorm.dyu5thdormdiscordbot.took_coin;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TookCoinGetBackTest {
    @Autowired
    TookCoinRepo repo;
    @Autowired
    TookCoinHandler handler;

    @Test
    void getBack() {

    }
}
