package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
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
    public void onGuildReady(GuildReadyEvent event) {
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
                Button.secondary(buttonIdSet.getMaintenance(), "維修模式(僅限開發人員使用)"),
                Button.secondary(buttonIdSet.getIpLookup(), "IP LOOKUP")
        ).queue();

        textChannel.sendMessage(
                "宿舍類別"
        ).addActionRow(
                Button.success(buttonIdSet.getGenerateReqAuth(), "重新生成驗證內容"),
                Button.primary(buttonIdSet.getGenerateRules(), "重新生成公約內容"),
                Button.success(buttonIdSet.getGenerateReqRepair(), "重新生成報修按鈕"),
                Button.success(buttonIdSet.getGenerateReqCadre(), "重新生成特定用戶專區按鈕")
        ).queue();

        textChannel.sendMessage(
                "宿舍類別 (2)"
        ).addActionRow(
                Button.primary(buttonIdSet.getFloorRoleCorrection(), "重新校正所有成員對應樓層身份組")
        ).queue();

        textChannel.sendMessage(
                "請假"
        ).addActionRow(
                Button.primary(buttonIdSet.getGenerateReqLev(), "重新生成請假按鈕"),
                Button.primary(buttonIdSet.getGenerateReqLevCadre(), "重新生成幹部審核按鈕")
        ).queue();
    }
}
