package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.Nullable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class ChannelOperation {
    public static void deleteAllMessage(TextChannel textChannel, int limit) {
        textChannel.getHistoryFromBeginning(limit).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );
    }

    @Nullable
    public static Message getMessage(TextChannel textChannel, String messageId) {
        return textChannel.retrieveMessageById(messageId)
                .onErrorMap(throwable -> null)
                .complete();
    }
}
