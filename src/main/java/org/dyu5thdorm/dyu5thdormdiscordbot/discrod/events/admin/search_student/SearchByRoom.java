package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.EmbedGenerator;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Optional;
import java.util.Set;

@Component
@PropertySource("classpath:discord.properties")
public class SearchByRoom extends ListenerAdapter {
    @Value("${component.button.student-info-by-bedId}")
    String buttonId;
    @Value("${component.modal.student-info-by-bedId}")
    String modalId;
    @Value("${component.modal.student-info-by-bedId-t}")
    String bedIdTextInputId;
    @Value("${regexp.room_id}")
    String roomRegex;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    DiscordLinkService discordLinkService;
    @Autowired
    EmbedGenerator embedGenerator;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals(buttonId)) return;

        event.replyModal(
                Modal.create(modalId, "以床號查詢住宿生")
                        .addActionRow(
                                TextInput.create(bedIdTextInputId, "房號", TextInputStyle.SHORT)
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
        if (!event.getModalId().equals(modalId)) return;

        String searchBedId = event.getValue(bedIdTextInputId).getAsString();

        if (!searchBedId.matches(roomRegex)) {
            event.reply("輸入的房號格式錯誤").setEphemeral(true).queue();
            return;
        }

        Set<LivingRecord> livingRecords = livingRecordService.findAllByBedIdContains(searchBedId);

        if (livingRecords.isEmpty()) {
            event.reply("查無結果").setEphemeral(true).queue();
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
            ).orElse(null);


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
