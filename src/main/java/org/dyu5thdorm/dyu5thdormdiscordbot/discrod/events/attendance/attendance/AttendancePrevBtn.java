package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AttendancePrevBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;
    final
    AttendanceEventUtils attendanceEventUtils;

    public AttendancePrevBtn(ButtonIdSet buttonIdSet, AttendanceHandler attendanceHandler, AttendanceEmbedBuilder attendanceEmbedBuilder, AttendanceEventUtils attendanceEventUtils) {
        this.buttonIdSet = buttonIdSet;
        this.attendanceHandler = attendanceHandler;
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.attendanceEventUtils = attendanceEventUtils;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendancePrev().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        if (event.getMessage().getEmbeds().isEmpty()) {
            event.getHook().sendMessage("NULL").setEphemeral(true).queue();
            return;
        }

        String roomId = attendanceEventUtils.getRoomIdFromMessage(event.getMessage());
        if (roomId == null) {
            event.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            return;
        }

        if (roomId.matches("^5[1-6]21$")) {
            event.getHook().sendMessage("已是最前").setEphemeral(true).queue();
            return;
        }

        Set<LivingRecord> prev = attendanceHandler.prevRoom(roomId);
        if (prev.isEmpty()) {
            event.getHook().sendMessage("已是最前").setEphemeral(true).queue();
            return;
        }
        attendanceEventUtils.sendAttendanceEmbed(event, prev);
    }
}
