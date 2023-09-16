package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.EmbedGenerator;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SearchByRoom extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ModalIdSet modalIdSet;
    @Value("${regexp.room_id}")
    String roomRegex;
    final LivingRecordService livingRecordService;
    final DiscordLinkService discordLinkService;
    final EmbedGenerator embedGenerator;

    public SearchByRoom(LivingRecordService livingRecordService, DiscordLinkService discordLinkService, EmbedGenerator embedGenerator, ButtonIdSet buttonIdSet, ModalIdSet modalIdSet) {
        this.livingRecordService = livingRecordService;
        this.discordLinkService = discordLinkService;
        this.embedGenerator = embedGenerator;
        this.buttonIdSet = buttonIdSet;
        this.modalIdSet = modalIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals(buttonIdSet.getSearchByBedId())) return;

        event.replyModal(
                Modal.create(modalIdSet.getSearchByBI(), "以床號查詢住宿生")
                        .addActionRow(
                                TextInput.create(modalIdSet.getFirstTextInput(), "住宿生房號", TextInputStyle.SHORT)
                                        .setPlaceholder("5126")
                                        .setMinLength(4)
                                        .setMaxLength(4)
                                        .setRequired(true)
                                        .build()
                        )
                        .build()
        ).queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals(modalIdSet.getSearchByBI())) return;

        String searchBedId = event.getValue(modalIdSet.getFirstTextInput()).getAsString();

        if (!searchBedId.matches(roomRegex)) {
            event.reply("輸入的房號格式錯誤").setEphemeral(true).queue();
            return;
        }

        Set<LivingRecord> livingRecords = livingRecordService.findAllByBedIdContains(searchBedId);

        if (livingRecords.isEmpty()) {
            event.reply("查無結果").setEphemeral(true).queue();
            return;
        }

        if (livingRecords.size() > 10) {
            event.reply("搜尋結果超過十筆，請縮小查詢範圍或尋找開發人員從後台查詢。").setEphemeral(true).queue();
            return;
        }

        for (LivingRecord livingRecord : livingRecords) {
            if (livingRecord.getStudent() == null) {
                event.getHook().sendMessage(String.format(
                        "%s 空床", livingRecord.getBed().getBedId()
                )).setEphemeral(true).queue();
                continue;
            }

            DiscordLink discordLink = discordLinkService.findByStudentId(
                    livingRecord.getStudent().getStudentId()
            );


            EmbedBuilder embedBuilder = embedGenerator.fromRoom(
                    livingRecord, discordLink
            );

            event.getHook().sendMessageEmbeds(
                    embedBuilder.build()
            ).setEphemeral(true).queue();
        }

        event.reply("查詢完畢").setEphemeral(true).queue();
    }
}
