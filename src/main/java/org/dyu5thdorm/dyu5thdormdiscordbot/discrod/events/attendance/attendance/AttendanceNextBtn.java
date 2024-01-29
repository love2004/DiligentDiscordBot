package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AttendanceNextBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;

    final
    AttendanceEventUtils attendanceEventUtils;

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendanceNext().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        String currentRoomId = attendanceEventUtils.getRoomIdFromMessage(event.getMessage());
        if (currentRoomId == null) {
            event.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            return;
        }

        Set<LivingRecord> next = attendanceHandler.nextRoom(currentRoomId);
        if (next.isEmpty()) {
            event.getHook().sendMessage("已是最後").setEphemeral(true).queue();
            return;
        }
        attendanceEventUtils.sendAttendanceEmbed(event, next);
    }
}
