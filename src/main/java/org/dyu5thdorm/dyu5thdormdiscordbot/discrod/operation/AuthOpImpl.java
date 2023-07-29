package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.operation;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:discord.properties")
public class AuthOpImpl implements Operation {
    @Value("${channel.auth}")
    String channelId;
    final DiscordAPI discordAPI;
    final ChannelOperation channelOperation;

    public AuthOpImpl(DiscordAPI discordAPI, ChannelOperation channelOperation) {
        this.discordAPI = discordAPI;
        this.channelOperation = channelOperation;
    }


    public void run()  {
        TextChannel textChannel = discordAPI.getJda().getTextChannelById(channelId);
        if (textChannel == null) {
            // TODO: HANDLE
            return;
        }
        String messageId = textChannel.getLatestMessageId();
        Message message = channelOperation.getMessage(textChannel, messageId);
        if (message == null || message.getButtons().size() == 0) {
            channelOperation.deleteAllMessage(textChannel, 100);
            channelOperation.sendOperationMessage(textChannel, ChannelOperation.Operation.AUTH);
        }
    }

}
