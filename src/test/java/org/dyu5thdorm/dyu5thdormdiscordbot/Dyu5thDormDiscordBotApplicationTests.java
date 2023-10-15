package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ReqLevOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.AttendanceRecordRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.LongLeaveRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.NoCallRollDateRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LeaveTempRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.NoCallRollDateService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TookCoinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    @Autowired
    ReqLevOperation reqLevOperation;
    @Autowired
    AttendanceHandler attendanceHandler;
    @Autowired
    LongLeaveRepo longLeaveRepo;
    @Autowired
    TookCoinRepo tookCoinRepo;

    @Test
    void contextLoads() {
        for (TookCoin tookCoin : tookCoinRepo.findAllByDateAndNotReturn(LocalDate.now())) {
            System.out.println(tookCoin);
        }
    }

}
