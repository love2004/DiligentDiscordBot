package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.stereotype.Component;

@Component
public class GenerateRequest extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;

    public GenerateRequest(ButtonIdSet buttonIdSet, ChannelIdSet channelIdSet) {
        this.buttonIdSet = buttonIdSet;
        this.channelIdSet = channelIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equalsIgnoreCase(buttonIdSet.getGenerateReqLev())) return;

        TextChannel reqLevChannel = event.getJDA().getTextChannelById(channelIdSet.getReqLev());

        if (reqLevChannel == null) {
            return;
        }

        reqLevChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        reqLevChannel.sendMessage("# 請假說明\n\n- 上課日 **(包含補假上課日、運動會)** 皆須點名\n- 無法到場點名者須依規定請假\n- 連續兩天點名應到未到者，將會通知家長")
                .addActionRow(
                        Button.primary("todo", "點名請假")
        ).queue();

        event.reply("Done").setEphemeral(true).queue();
    }
}
