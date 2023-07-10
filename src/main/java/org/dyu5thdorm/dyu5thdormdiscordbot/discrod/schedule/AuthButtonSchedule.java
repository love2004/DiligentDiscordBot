package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.schedule;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

import static org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation.deleteAllMessage;
import static org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation.getMessage;

@Component
@PropertySource("classpath:discord.properties")
public class AuthButtonSchedule implements DiscordSchedule{
    @Autowired
    DiscordAPI discordAPI;
    @Value("${image.auth}")
    String imagePath;
    @Value("${channel.auth}")
    String channelId;
    @Value("${component.button.auth}")
    String buttonId;


    @Scheduled(fixedRateString = "${schedule.auto}")
    public void run()  {
        TextChannel textChannel = discordAPI.getJda().getTextChannelById(channelId);
        if (textChannel == null) {
            // TODO: HANDLE
            return;
        }
        String messageId = textChannel.getLatestMessageId();
        Message message = getMessage(textChannel, messageId);
        if (message == null || message.getButtons().size() == 0) {
            deleteAllMessage(textChannel, 100);
            createNewOne(textChannel);
        }
    }

    void createNewOne(TextChannel textChannel) {
        textChannel.sendMessage(imagePath)
                .setActionRow(
                        Button.primary(buttonId, "驗證身份")
                ).queue();
    }
}
