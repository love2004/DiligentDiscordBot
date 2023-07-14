package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.schedule.buttons;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.schedule.DiscordSchedule;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@PropertySource("classpath:discord.properties")
@Component
public class AdminOpSchedule implements DiscordSchedule {
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
    final DiscordAPI discordAPI;
    final ChannelOperation channelOperation;

    public AdminOpSchedule(DiscordAPI discordAPI, ChannelOperation channelOperation) {
        this.discordAPI = discordAPI;
        this.channelOperation = channelOperation;
    }

    @Scheduled(fixedRate = 60000)
    public void run() {
        TextChannel textChannel = discordAPI.getJda().getTextChannelById(adminButtonChannelId);

        String messageId = textChannel.getLatestMessageId();

        Message message = channelOperation.getMessage(textChannel, messageId);

        if (message == null || message.getButtons().size() == 0) {
            channelOperation.deleteAllMessage(textChannel, 100);
            channelOperation.sendOperationMessage(textChannel, ChannelOperation.Operation.ADMIN_STUDENT_INFO);
        }
    }
}
