package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentOperationEvent extends ListenerAdapter {
    final
    ChannelIdSet channelIdSet;
    final
    ButtonIdSet buttonIdSet;

    public DevelopmentOperationEvent(ButtonIdSet buttonIdSet, ChannelIdSet channelIdSet) {
        this.buttonIdSet = buttonIdSet;
        this.channelIdSet = channelIdSet;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        TextChannel textChannel = event.getGuild().getTextChannelById(channelIdSet.getAdminOperation());
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
                Button.danger(buttonIdSet.getShutdown(), "關機"),
                Button.secondary(buttonIdSet.getMaintenance(), "維修模式"),
                Button.secondary(buttonIdSet.getIpLookup(), "IP LOOKUP")
        ).queue();

        textChannel.sendMessage(
                "重新產生頻道內容 (1)"
        ).addActionRow(
                Button.success(buttonIdSet.getGenerateReqAuth(), "驗證"),
                Button.primary(buttonIdSet.getGenerateRules(), "規章"),
                Button.success(buttonIdSet.getGenerateReqRepair(), "報修"),
                Button.success(buttonIdSet.getGenerateReqCadre(), "特定用戶")
        ).queue();

        textChannel.sendMessage(
                "重新產生頻道內容 (2)"
        ).addActionRow(
                Button.primary(buttonIdSet.getGenerateReqLevCadre(), "點名"),
                Button.primary(buttonIdSet.getGenerateReqLev(), "請假")
        ).queue();

        textChannel.sendMessage(
                "身份組"
        ).addActionRow(
                Button.primary(buttonIdSet.getFloorRoleCorrection(), "校正樓層身分組")
        ).queue();
    }
}
