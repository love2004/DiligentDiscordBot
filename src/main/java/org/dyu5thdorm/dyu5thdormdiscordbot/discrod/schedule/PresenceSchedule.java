package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.schedule;

import net.dv8tion.jda.api.entities.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:presence.properties")
public class PresenceSchedule implements DiscordSchedule {
    @Value("${sorryyoth}")
    private List<String> songs;
    private int index;
    @Autowired
    private DiscordAPI discordAPI;

    @Scheduled(fixedRate = 60000)
    public void run() {
        discordAPI.getJda().getPresence().setActivity(
                Activity.listening(
                        "拍謝少年-"+songs.get(
                                index + 1 > 25 ? index = 0 : index++
                        )
                )
        );
    }
}
