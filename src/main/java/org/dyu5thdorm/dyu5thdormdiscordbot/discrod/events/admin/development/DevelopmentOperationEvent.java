package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentOperationEvent extends ListenerAdapter {
    @Value("${channel.admin.operation}")
    String operationChannelId;
    @Value("${component.button.shutdown}")
    String shutdownButtonId;
    @Value("${component.button.generate-rules}")
    String generateRulesButtonId;
    @Value("${component.button.generate-request-leave}")
    String generateReqLevButtonId;
    @Value("${component.button.generate-request-auth}")
    String generateAuthButtonId;
    @Value("${component.button.generate-request-repair}")
    String generateRepairButtonId;
    @Value("${component.button.generate-request-admin}")
    String generateAdminButtonId;

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        TextChannel textChannel = event.getGuild().getTextChannelById(operationChannelId);
        if (textChannel == null) return;

        Message latestMessage = textChannel.retrieveMessageById(textChannel.getLatestMessageId())
                .onErrorMap(throwable -> null)
                .complete();

        if (latestMessage != null && latestMessage.getAuthor().isBot()) return;

        textChannel.getIterableHistory().complete().forEach(
                message -> message.delete().queue()
        );

        textChannel.sendMessage(
                "機器人類別"
        ).addActionRow(
                Button.danger(shutdownButtonId, "關機")
        ).queue();

        textChannel.sendMessage(
                "宿舍類別"
        ).addActionRow(
                Button.success(generateAuthButtonId, "重新生成驗證內容"),
                Button.primary(generateRulesButtonId, "重新生成公約內容"),
                Button.success(generateRepairButtonId, "重新生成報修按鈕"),
                Button.primary(generateReqLevButtonId, "重新生成請假按鈕"),
                Button.success(generateAdminButtonId, "重新生成管理員按鈕")
        ).queue();
    }
}
