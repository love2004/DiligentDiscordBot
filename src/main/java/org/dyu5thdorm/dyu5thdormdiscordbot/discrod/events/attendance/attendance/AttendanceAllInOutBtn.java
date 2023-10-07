package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
public class AttendanceAllInOutBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;
    final
    AttendanceEventUtils attendanceEventUtils;

    public AttendanceAllInOutBtn(AttendanceHandler attendanceHandler, ButtonIdSet buttonIdSet, AttendanceEmbedBuilder attendanceEmbedBuilder, AttendanceEventUtils attendanceEventUtils) {
        this.attendanceHandler = attendanceHandler;
        this.buttonIdSet = buttonIdSet;
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.attendanceEventUtils = attendanceEventUtils;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendanceAllIn().equalsIgnoreCase(eventButtonId) &&
                !buttonIdSet.getAttendanceAllOut().equalsIgnoreCase(eventButtonId)) return;
        event.deferReply(true).queue();

        if (attendanceHandler.isAfter(LocalTime.now())) {
            attendanceEventUtils.sendEndTime(event, false);
            return;
        }

        if (attendanceEventUtils.getRoomIdFromMessage(event.getMessage()) == null) {
            event.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            return;
        }

        List<String> bedIds = this.getBedIdFromEmbedMessage(
                event.getMessage().getEmbeds().get(0)
        );

        attendanceHandler.doAttendance(
                event.getUser().getId(),
                bedIds,
                isInStatus(eventButtonId)
        );

        Set<LivingRecord> next = attendanceHandler.getNotComplete(event.getUser().getId());
        attendanceEventUtils.showNextRoom(event, next);
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
