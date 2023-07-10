package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LivingRecordRepository;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@PropertySource("classpath:discord.properties")
class Dyu5thDormDiscordBotApplicationTests {
    @Autowired
    LivingRecordService recordRepository;

    @Test
    void contextLoads() {
        System.out.println(recordRepository.findAllByBedId("5125"));
    }

}
