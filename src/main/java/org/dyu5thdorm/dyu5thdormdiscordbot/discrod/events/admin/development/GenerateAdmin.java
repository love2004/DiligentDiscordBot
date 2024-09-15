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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateAdmin extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;
    final
    ChannelOperation channelOperation;

    @Value("${school_year}")
    String schoolYear;
    @Value("${semester}")
    String semester;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getGenerateReqCadre().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(
                channelIdSet.getCadreButton()
        );

        if (textChannel == null) return;
        channelOperation.deleteAllMessage(textChannel, 100);

        textChannel.sendMessage(
                String.format(
                        """
                        :mag: 以 帳號\\\\房號\\\\學號\\\\姓名 查詢住宿生(模糊查詢)
                        > 僅能查詢 %s-%s 學期的業勤住宿生
                        """, schoolYear, semester
                )
        ).addComponents(
                ActionRow.of(
                        Button.primary(buttonIdSet.getSearchByDiscordId(), "帳號"),
                        Button.success(buttonIdSet.getSearchByBedId(), "房號"),
                        Button.success(buttonIdSet.getSearchByStudentId(), "學號"),
                        Button.success(buttonIdSet.getSearchByStudentName(), "姓名")
                )
        ).queue();

        event.getHook().sendMessage("DONE").setEphemeral(true).queue();
    }
}
