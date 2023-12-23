package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Lottery;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ReqLevOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.*;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.*;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.view.TicketView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

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

    @Autowired
    ActivityPartiRepo activitiesPartiRepo;
    @Autowired
    ActivityService activityService;
    @Autowired
    TicketViewRepo ticketViewRepo;
    @Autowired
    Lottery lottery;

    @Test
    void contextLoads() throws FileNotFoundException {
        PrintStream stream = new PrintStream("winner.txt");

        List<TicketView> allTicketViews = ticketViewRepo.findAll();
        for (int i = 0; i < 1000000; i++) {
            var winners = lottery.drawWinners(allTicketViews, 1);
            winners.forEach(stream::println);
        }
    }

}
