package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceStatusEnum;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AttendanceAllInOutBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;
    @Value("${regexp.bed_id}")
    String bedIdRegex;

    public AttendanceAllInOutBtn(AttendanceHandler attendanceHandler, ButtonIdSet buttonIdSet, AttendanceEmbedBuilder attendanceEmbedBuilder) {
        this.attendanceHandler = attendanceHandler;
        this.buttonIdSet = buttonIdSet;
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendanceAllIn().equalsIgnoreCase(eventButtonId) &&
                !buttonIdSet.getAttendanceAllOut().equalsIgnoreCase(eventButtonId)) return;
        event.deferReply(true).queue();
        Optional<MessageEmbed> eventEmbed = event.getMessage().getEmbeds().stream().findFirst();
        if (eventEmbed.isEmpty()) {
            event.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            return;
        }
        boolean isIn = isInStatus(eventButtonId);
        List<String> bedId = this.getBedIdFromEmbedMessage(
                event.getMessage().getEmbeds().get(0)
        );
        for (String s : bedId) {
            if (!s.matches(bedIdRegex)) continue;
            attendanceHandler.attendance(
                    s,
                    isIn ? AttendanceStatusEnum.IN : AttendanceStatusEnum.OUT,
                    event.getUser().getId()
            );
        }
        if (eventEmbed.get().getFooter() == null) {
            event.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            return;
        }
        String currentRoomId = eventEmbed.get().getFooter().getText();
        Set<LivingRecord> next = attendanceHandler.nextRoom(currentRoomId);
        // TODO: 4CD 點完不會結束
        if (next.isEmpty()) {
            event.getHook().sendMessage("點完名啦～ 辛苦啦～ 太感謝啦～").setEphemeral(true).queue();
            return;
        }
        MessageEmbed roomEmbed = attendanceEmbedBuilder.getByLivingRecord(next).build();
        if (roomEmbed.getFooter() == null) {
            event.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            return;
        }
        boolean isLastRoom = attendanceHandler.isLastRoom(roomEmbed.getFooter().getText());
        boolean isEmptyRoom = attendanceHandler.isEmptyRoom(next);
        event.getHook().sendMessageEmbeds(
                roomEmbed
        ).setActionRow(
                attendanceEmbedBuilder.getAttendanceActionRow(isLastRoom, isEmptyRoom)
        ).queue();
    }

    boolean isInStatus(@NotNull String buttonId) {
        return buttonIdSet.getAttendanceAllIn().equalsIgnoreCase(buttonId);
    }

    List<String> getBedIdFromEmbedMessage(@NotNull MessageEmbed embed) {
        return embed.getFields().stream().map(
                MessageEmbed.Field::getName
        ).toList();
    }
}
