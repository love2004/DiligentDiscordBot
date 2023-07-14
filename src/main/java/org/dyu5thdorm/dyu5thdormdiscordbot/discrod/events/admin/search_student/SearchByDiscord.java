package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;

import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.EmbedGenerator;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@PropertySource("classpath:discord.properties")
public class SearchByDiscord extends ListenerAdapter {
    @Value("${component.button.student-info-by-discord}")
    String discordIdButtonId;
    @Value("${component.menu.student-info-by-discord}")
    String menuId;
    final LivingRecordService livingRecordService;
    final EmbedGenerator embedGenerator;

    public SearchByDiscord(LivingRecordService livingRecordService, EmbedGenerator embedGenerator) {
        this.livingRecordService = livingRecordService;
        this.embedGenerator = embedGenerator;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if (buttonId == null) return;

        if (!buttonId.equals(discordIdButtonId)) return;

        event.replyComponents(
                ActionRow.of(
                        EntitySelectMenu.create(menuId, EntitySelectMenu.SelectTarget.USER)
                                .setPlaceholder("請選擇一位帳號進行查詢")
                                .build()
                )
        ).setEphemeral(true).queue();
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (!event.getSelectMenu().getId().equals(menuId)) return;

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
