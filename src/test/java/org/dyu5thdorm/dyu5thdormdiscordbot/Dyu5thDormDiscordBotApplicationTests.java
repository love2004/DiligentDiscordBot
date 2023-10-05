package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.AttendanceRecordRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.NoCallRollDateRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LeaveTempRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.NoCallRollDateService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TookCoinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;

@SpringBootTest
class Dyu5thDormDiscordBotApplicationTests {
    @Autowired
    LeaveTempRecordService leaveTempRecordService;
    @Autowired
    NoCallRollDateService service;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    NoCallRollDateService noCallRollDateService;
    @Autowired
    NoCallRollDateRepo noCallRollDateRepository;
    @Autowired
    TookCoinService tookCoinService;
    @Autowired
    AttendanceRecordRepo attendanceRecordRepository;

    @Test
    void contextLoads() {
        LocalDate startDate = LocalDate.now(); // 起始點名日期 2023/10/04
        LocalDate endDate = LocalDate.of(2024, 1, 11); // 最後點名日期 2023/01/11
        int thursday = 0;
        while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
            if (startDate.getDayOfWeek() == DayOfWeek.THURSDAY) thursday++;
            startDate = startDate.plusDays(1L);
        }
        int everyTimeCost = 85;
        System.out.println(
                everyTimeCost * thursday
        );
    }

}
