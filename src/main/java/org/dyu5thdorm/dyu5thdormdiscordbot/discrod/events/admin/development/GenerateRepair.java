package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateRepair extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;
    final ChannelOperation channelOperation;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!buttonIdSet.getGenerateReqRepair().equalsIgnoreCase(event.getButton().getId())) return;
        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(channelIdSet.getRepair());
        if (textChannel == null) {
            // TODO: HANDLE
            return;
        }

        channelOperation.deleteAllMessage(textChannel, 100);

        textChannel.sendMessage("""
                        # 各類報修(水電土木、洗、烘衣機、販賣機、飲水機)

                        - 報修會自動帶入您的個人資料，無需額外填寫。
                        - *報修後請勿再次重複填寫，如需更改請聯絡宿舍幹部*
                        - 個人區域維修**請務必填上可配合維修時間**
                         - 若未填上，值班幹部將配合維修人員領備用鑰匙進入維修。
                        - 區域說明：
                         - AB區：靠近**大門**
                         - CD區：靠近**後門**""")
                .addActionRow(
                        Button.success(buttonIdSet.getRepair(), "各類報修 Repair report")
                                .withEmoji(Emoji.fromUnicode("U+1F9F0"))
                ).queue();

        event.getHook().sendMessage("DONE").setEphemeral(true).queue();
    }
}
