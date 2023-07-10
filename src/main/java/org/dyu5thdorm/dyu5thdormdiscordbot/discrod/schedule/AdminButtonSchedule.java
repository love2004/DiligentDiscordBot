package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.schedule;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation.deleteAllMessage;
import static org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation.getMessage;

@PropertySource("classpath:discord.properties")
@Component
public class AdminButtonSchedule implements DiscordSchedule {
    @Autowired
    DiscordAPI discordAPI;
    @Value("${channel.admin_button}")
    String adminButtonChannelId;
    @Value("${component.button.student-info-by-discord}")
    String discordIdButtonId;
    @Value("${component.button.student-info-by-bedId}")
    String bedIdButtonId;
    @Value("${component.button.student-info-by-studentId}")
    String studentIdButtonId;
    @Value("${component.button.student-info-by-name}")
    String nameButtonId;

    @Scheduled(fixedRate = 60000)
    public void run() {
        TextChannel textChannel = discordAPI.getJda().getTextChannelById(adminButtonChannelId);

        String messageId = textChannel.getLatestMessageId();

        Message message = getMessage(textChannel, messageId);

        if (message == null || message.getButtons().size() == 0) {
            deleteAllMessage(textChannel, 100);
            createNewOne(textChannel);
        }
    }

    void createNewOne(TextChannel textChannel) {
        textChannel.sendMessage(":mag: 查詢住宿生")
                .addActionRow(
                        Button.primary(discordIdButtonId, "以帳號查詢")
                )
                .addActionRow(
                        Button.success(bedIdButtonId, "以房號查詢")
                )
                .addActionRow(
                        Button.success(studentIdButtonId, "以學號查詢")
                )
                .addActionRow(
                        Button.success(nameButtonId, "以姓名查詢")
                )
                .queue();
    }
}
