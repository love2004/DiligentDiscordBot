package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Dyu5thDormDiscordBotApplication {
    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Dyu5thDormDiscordBotApplication.class, args);
    }

}
