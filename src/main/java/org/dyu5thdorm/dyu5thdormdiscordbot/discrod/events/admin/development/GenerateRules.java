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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateRules extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;
    final ChannelOperation channelOperation;

    @Value("${link.rules.zh-tw}")
    String rulesLinkZHTW;
    @Value("${link.rules.en}")
    String rulesLinkEn;
    @Value("${school_year}")
    String schoolYear;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!buttonIdSet.getGenerateRules().equalsIgnoreCase(event.getButton().getId())) return;
        event.deferReply().setEphemeral(true).queue();

        TextChannel rulesChannel = event.getJDA().getTextChannelById(channelIdSet.getRules());

        if (rulesChannel == null) {
            event.reply("無法生成，頻道不存在").setEphemeral(true).queue();
            return;
        }
        channelOperation.deleteAllMessage(rulesChannel, 100);

        rulesChannel.sendMessage(
                String.format(
                """
                # %s 學年大葉大學業勤學舍住宿公約及違規處理要點
                > Da-Yeh University Accommodation Rules (%sth Year)
                ### 請所有住宿生詳細閱讀住宿公約，以確保了解自己的權益和義務。
                > Residents, please read the agreement to understand your rights and duties.
                ### 宿舍有權在必要時對住宿公約進行修改或更新。
                > The dorm reserves the right to modify or update the agreement when necessary.
                ### 公約及處理要點連結如下，請詳閱：
                > The agreement links are below. Please review carefully:
                
                """, this.schoolYear, this.schoolYear)
        ).addActionRow(
                Button.link(rulesLinkZHTW, Emoji.fromUnicode("U+1F4D6"))
                        .withLabel(
                                "正體中文"
                        ),
                Button.link(rulesLinkEn, Emoji.fromUnicode("U+1F4D6"))
                        .withLabel(
                                "English"
                        )
        ).queue();

        event.getHook().sendMessage("DONE").queue();
    }
}
