package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GenerateAdmin extends ListenerAdapter {
    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    ChannelIdSet channelIdSet;
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

        textChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        textChannel.sendMessage(
                String.format(
                        ":mag: 查詢住宿生(模糊查詢)\n > 僅能查詢 %s-%s 學期的住宿生", schoolYear, semester
                )
        ).addComponents(
                ActionRow.of(
                        Button.primary(buttonIdSet.getSearchByDiscordId(), "以帳號查詢"),
                        Button.success(buttonIdSet.getSearchByBedId(), "以房號查詢"),
                        Button.success(buttonIdSet.getSearchByStudentId(), "以學號查詢"),
                        Button.success(buttonIdSet.getSearchByStudentName(), "以姓名查詢")
                )
        ).queue();

        textChannel.sendMessage(
                String.format(
                        "特殊類別", schoolYear, semester
                )
        ).addComponents(
                ActionRow.of(
                        Button.danger(buttonIdSet.getTookCoinReturn(), "廠商卡幣已退費日期登記")
                )
        ).queue();

        event.getHook().sendMessage("DONE").setEphemeral(true).queue();
    }
}
