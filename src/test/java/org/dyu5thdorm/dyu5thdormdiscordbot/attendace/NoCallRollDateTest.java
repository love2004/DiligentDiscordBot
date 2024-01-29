package org.dyu5thdorm.dyu5thdormdiscordbot.attendace;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.NoCallRollDateRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;

@SpringBootTest
public class NoCallRollDateTest {
    @Autowired
    NoCallRollDateRepo repo;

    @Test
    void add() {

    }

    @Test
    void list() {
        repo.findAll().forEach(
                noCallRollDate -> {
                    LocalDate day = noCallRollDate.getDay();
                    if (day.getDayOfWeek() == DayOfWeek.SATURDAY) System.out.println(day);
                }
        );
    }
}
