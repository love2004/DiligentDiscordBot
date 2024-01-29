package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AttendanceBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;
    final
    LivingRecordService livingRecordService;
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEventUtils attendanceEventUtils;
    final
    Maintenance maintenance;


    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendance().equalsIgnoreCase(eventButtonId)) return;
        event.deferReply().setEphemeral(true).queue();

        boolean isIllegalTime = attendanceHandler.isIllegalTime(LocalTime.now()) || attendanceHandler.isNoCallNoDay(LocalDate.now());
        if (!attendanceHandler.isDevMode() && isIllegalTime) {
            attendanceEventUtils.sendStartTime(event);
            return;
        }

        Set<LivingRecord> startRoom;
        Optional<AttendanceHandler.ErrorType> errorType = attendanceHandler.check(event.getUser().getId());

        if (errorType.isEmpty()) {
            startRoom = attendanceHandler.getRoomStart(event.getUser().getId());
            attendanceEventUtils.sendAttendanceEmbed(event, startRoom);
            return;
        }

        AttendanceHandler.ErrorType error = errorType.get();

        event.getHook().sendMessage(
                String.format(
                        """
                        %s
                        > 若有任何問題，請聯絡本群開發人員。
                        """, error.getMessage()
                )
        ).queue();
    }

}
