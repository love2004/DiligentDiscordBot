package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.stereotype.Component;

@Component
public class GenerateRepair extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;

    public GenerateRepair(ButtonIdSet buttonIdSet, ChannelIdSet channelIdSet) {
        this.buttonIdSet = buttonIdSet;
        this.channelIdSet = channelIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (eventButtonId == null || !eventButtonId.equalsIgnoreCase(buttonIdSet.getGenerateReqRepair())) return;

        TextChannel textChannel = event.getJDA().getTextChannelById(channelIdSet.getRepair());
        if (textChannel == null) {
            // TODO: HANDLE
            return;
        }

        textChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        textChannel.sendMessage("# 各類報修(水電土木、洗、烘衣機、販賣機、飲水機)\n\n- 報修會自動帶入您的個人資料，無需額外填寫。\n- 個人區域維修**請務必填上可配合維修時間**\n - 若未填上，值班幹部將配合維修人員領備用鑰匙進入維修。\n- 區域說明：\n - AB區：靠近**大門**\n - CD區：靠近**後門**")
                .addActionRow(
                        Button.success(buttonIdSet.getRepair(), "各類報修 Repair report")
                                .withEmoji(Emoji.fromUnicode("U+1F9F0"))
                ).addActionRow(
                        Button.primary(buttonIdSet.getTookCoin(), "吃錢登記")
                                .withEmoji(Emoji.fromUnicode("U+1F4B8"))
                ).queue();
        event.reply("DONE").setEphemeral(true).queue();
    }
}
