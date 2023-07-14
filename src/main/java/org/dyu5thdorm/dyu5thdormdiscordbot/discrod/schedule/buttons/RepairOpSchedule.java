package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.schedule.buttons;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.schedule.DiscordSchedule;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:discord.properties")
public class RepairOpSchedule implements DiscordSchedule {
    @Value("${channel.repair}")
    String repairChannelId;
    final DiscordAPI discordAPI;
    final ChannelOperation channelOperation;

    public RepairOpSchedule(DiscordAPI discordAPI, ChannelOperation channelOperation) {
        this.discordAPI = discordAPI;
        this.channelOperation = channelOperation;
    }

    @Scheduled(fixedRateString = "${schedule.auto}")
    @Override
    public void run() {
        TextChannel textChannel = discordAPI.getJda().getTextChannelById(repairChannelId);
        if (textChannel == null) {
            // TODO: HANDLE
            return;
        }
        String messageId = textChannel.getLatestMessageId();
        Message message = channelOperation.getMessage(textChannel, messageId);
        if (message == null || message.getButtons().size() == 0) {
            channelOperation.deleteAllMessage(textChannel, 100);
            channelOperation.sendOperationMessage(textChannel, ChannelOperation.Operation.REPAIR);
        }
    }
}
