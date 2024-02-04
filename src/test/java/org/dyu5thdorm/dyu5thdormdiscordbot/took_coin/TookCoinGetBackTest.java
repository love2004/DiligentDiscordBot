package org.dyu5thdorm.dyu5thdormdiscordbot.took_coin;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
public class TookCoinGetBackTest {
    @Autowired
    TookCoinRepo repo;
    @Autowired
    TookCoinHandler handler;

    @Test
    void getBack() {
        try {
            var a = Files.readString(
                    Path.of("/Users/nutt1101/Documents/dormitory/java/Dyu5thDormDiscordBot/files/machine/washing_and_dryer/v-2024-02-04.csv")
            );
            System.out.printf(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
