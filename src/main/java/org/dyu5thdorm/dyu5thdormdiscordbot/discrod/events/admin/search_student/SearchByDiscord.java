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
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
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
        String buttonId = event.getButton().getId();
        if (buttonId == null) return;

        if (!buttonId.equals(buttonIdSet.getSearchByDiscordId())) return;

        event.replyComponents(
                ActionRow.of(
                        EntitySelectMenu.create(menuIdSet.getInfoByDiscordAccOption(), EntitySelectMenu.SelectTarget.USER)
                                .setPlaceholder("請選擇一位帳號進行查詢")
                                .build()
                )
        ).setEphemeral(true).queue();
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (!event.getSelectMenu().getId().equals(menuIdSet.getInfoByDiscordAccOption())) return;

        List<User> userLists =  event.getMentions().getUsers();

        for (User userList : userLists) {
            String userId = userList.getId();
            LivingRecord livingRecord = livingRecordService.findLivingRecordByDiscordId(userId);
            if (livingRecord == null) {
                event.reply("查無結果").setEphemeral(true).queue();
                return;
            }
            EmbedBuilder embedBuilder = embedGenerator.fromDiscord(livingRecord, userId);
            event.replyEmbeds(
                    embedBuilder.build()
            ).setEphemeral(true).queue();
        }
    }
}
