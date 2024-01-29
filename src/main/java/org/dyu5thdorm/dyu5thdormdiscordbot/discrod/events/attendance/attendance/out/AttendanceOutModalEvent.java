package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance.AttendanceEventUtils;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AttendanceOutModalEvent extends ListenerAdapter {
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEventUtils attendanceEventUtils;
    final
    ModalIdSet modalIdSet;

    @Value("${regexp.bed-set}")
    String bedIdSetRegex;

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!modalIdSet.getAttendanceOut().equalsIgnoreCase(eventModalId)) return;

        event.deferReply(true).queue();

        String roomId = event.getValue(modalIdSet.getFirstTextInput()).getAsString();
        String bedSet = event.getValue(modalIdSet.getSecondTextInput()).getAsString();
        if (!bedSet.matches(bedIdSetRegex)) {
            event.getHook().sendMessage("床位格式錯誤").setEphemeral(true).queue();
            return;
        }

        attendanceHandler.doAttendance(
                event.getUser().getId(),
                roomId,
                getBedIdSet(bedSet.toUpperCase())
        );

        Set<LivingRecord> next = attendanceHandler.getNotComplete(event.getUser().getId());
        attendanceEventUtils.showNextRoom(event, next);
    }

    Set<Character> getBedIdSet(@NotNull String bed) {
        Set<Character> bedIdSet = new HashSet<>();
        for (int i = 0; i < bed.length(); i++) {
            bedIdSet.add(
                    bed.charAt(i)
            );
        }
        return bedIdSet;
    }
}
