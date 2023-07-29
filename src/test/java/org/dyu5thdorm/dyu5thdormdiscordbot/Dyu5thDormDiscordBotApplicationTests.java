package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.line.LineNotify;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.RepairTokenSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LivingRecordRepository;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.models.RepairParameter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class Dyu5thDormDiscordBotApplicationTests {
    @Test
    void contextLoads() throws UnsupportedEncodingException {

    }

}
