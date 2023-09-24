package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.EmbedGenerator;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SearchByDiscord extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    MenuIdSet menuIdSet;
    final LivingRecordService livingRecordService;
    final EmbedGenerator embedGenerator;

    public SearchByDiscord(LivingRecordService livingRecordService, EmbedGenerator embedGenerator, ButtonIdSet buttonIdSet, MenuIdSet menuIdSet) {
        this.livingRecordService = livingRecordService;
        this.embedGenerator = embedGenerator;
        this.buttonIdSet = buttonIdSet;
        this.menuIdSet = menuIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getSearchByDiscordId().equalsIgnoreCase(eventButtonId)) return;
        event.deferReply().setEphemeral(true).queue();
        event.getHook().sendMessageComponents(
                ActionRow.of(
                        EntitySelectMenu.create(menuIdSet.getInfoByDiscordAccOption(), EntitySelectMenu.SelectTarget.USER)
                                .setPlaceholder("請選擇一位帳號進行查詢")
                                .build()
                )
        ).setEphemeral(true).queue();
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        String eventMenuId = event.getSelectMenu().getId();
        if (!menuIdSet.getInfoByDiscordAccOption().equalsIgnoreCase(eventMenuId)) return;
        event.deferReply().setEphemeral(true).queue();

        List<User> userLists =  event.getMentions().getUsers();

        for (User userList : userLists) {
            String userId = userList.getId();
            LivingRecord livingRecord = livingRecordService.findLivingRecordByDiscordId(userId);
            if (livingRecord == null) {
                event.getHook().sendMessage("查無結果").setEphemeral(true).queue();
                return;
            }
            EmbedBuilder embedBuilder = embedGenerator.fromDiscord(livingRecord, userId);
            event.getHook().sendMessageEmbeds(
                    embedBuilder.build()
            ).setEphemeral(true).queue();
        }
    }
}
