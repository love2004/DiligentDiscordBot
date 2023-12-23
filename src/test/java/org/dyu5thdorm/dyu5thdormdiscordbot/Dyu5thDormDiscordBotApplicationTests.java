package org.dyu5thdorm.dyu5thdormdiscordbot;

import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ReqLevOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.*;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

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

    @Test
    void contextLoads() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("tickets"));
        List<String> ticketBox = new ArrayList<>();
        while (scanner.hasNextLine()) {
            ticketBox.add(scanner.next());
        }
        Collections.shuffle(ticketBox);

        Random random = new Random();
        List<String> winners = new ArrayList<>();
        PrintStream stream = new PrintStream("winner.txt");
        int count = 0;
        while (count < 100000) {
            String winner = ticketBox.get(random.nextInt(ticketBox.size()));
            if (winners.contains(winner)) continue;
            else winners.add(winner);
            if (winners.size() == 3) {
                stream.println(String.join(",", winners));
                winners.clear();
                count++;
            }
        }
    }

}
