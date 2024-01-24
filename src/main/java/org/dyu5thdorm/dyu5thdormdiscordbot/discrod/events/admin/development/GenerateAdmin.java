package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GenerateAdmin extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;
    @Value("${school_year}")
    String schoolYear;
    @Value("${semester}")
    String semester;

    public GenerateAdmin(ButtonIdSet buttonIdSet, ChannelIdSet channelIdSet) {
        this.buttonIdSet = buttonIdSet;
        this.channelIdSet = channelIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getGenerateReqCadre().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(
                channelIdSet.getCadreButton()
        );

        if (textChannel == null) return;

        textChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

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

        textChannel.sendMessage("特殊類別")
                .addComponents(
                        ActionRow.of(
                                Button.danger(buttonIdSet.getTookCoinReturn(), "廠商退幣日期登記")
                        )
                ).queue();

        event.getHook().sendMessage("DONE").setEphemeral(true).queue();
    }
}
