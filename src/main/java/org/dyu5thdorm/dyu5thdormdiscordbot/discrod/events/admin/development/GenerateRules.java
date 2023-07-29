package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GenerateRules extends ListenerAdapter {
    @Value("${component.button.generate-rules}")
    String generateButtonId;
    @Value("${channel.rules}")
    String rulesChannelId;
    @Value("${image.rules}")
    String rulesImageLink;
    @Value("${link.rules}")
    String rulesLink;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals(generateButtonId)) return;

        TextChannel rulesChannel = event.getJDA().getTextChannelById(rulesChannelId);

        if (rulesChannel == null) {
            event.reply("無法生成，頻道不存在").setEphemeral(true).queue();
            return;
        }

        rulesChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        rulesChannel.sendMessage("# 112學年大葉大學業勤學舍住宿公約及違規處理要點\n\n- 請所有住宿生詳細閱讀住宿公約，以確保了解自己的權益和義務。\n\n- 宿舍有權在必要時對住宿公約進行修改或更新。\n\t- 任何對公約的變更都將以Discord內全體廣播的方式通知所有住宿生。").addActionRow(
                Button.link(rulesLink, Emoji.fromUnicode("U+1F4D6"))
                        .withLabel("112學年大葉大學業勤學舍住宿公約及違規處理要點")
        ).queue();

        event.reply("DONE").setEphemeral(true).queue();
    }
}
