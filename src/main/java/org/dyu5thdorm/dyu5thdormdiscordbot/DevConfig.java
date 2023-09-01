package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("dev")
@PropertySource("classpath:discord-dev.properties")
public class DevConfig {
}

