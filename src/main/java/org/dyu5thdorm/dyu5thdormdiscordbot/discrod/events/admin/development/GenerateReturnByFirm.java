package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateReturnByFirm extends ListenerAdapter {
    final ButtonIdSet buttonIdSet;
    final ChannelIdSet channelIdSet;
    final ChannelOperation channelOperation;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if (!buttonIdSet.getReturnByFirm().equalsIgnoreCase(buttonId)) return;

        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(channelIdSet.getReturnByFirm());

        if (textChannel == null) return;
        channelOperation.deleteAllMessage(textChannel, 100);

        textChannel.sendMessageComponents(
                ActionRow.of(
                        Button.danger(buttonIdSet.getTookCoinReturn(), "廠商退幣日期登記")
                )
        ).queue();

        event.getHook().sendMessage("DONE").queue();
    }
}
