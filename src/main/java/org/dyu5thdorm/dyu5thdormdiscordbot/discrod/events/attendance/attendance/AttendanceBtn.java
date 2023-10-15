package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

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
import java.util.Set;

@Component
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

    public AttendanceBtn(ButtonIdSet buttonIdSet, AttendanceEmbedBuilder attendanceEmbedBuilder, LivingRecordService livingRecordService, AttendanceHandler attendanceHandler, AttendanceEventUtils attendanceEventUtils, Maintenance maintenance) {
        this.buttonIdSet = buttonIdSet;
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.livingRecordService = livingRecordService;
        this.attendanceHandler = attendanceHandler;
        this.attendanceEventUtils = attendanceEventUtils;
        this.maintenance = maintenance;
    }


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

        Set<LivingRecord> startRoom = attendanceHandler.getRoomStart(event.getUser().getId());
        if (startRoom == null) {
            event.getHook().sendMessage("""
                    有以下二個原因導致您無法使用此功能：
                    1. 您非宿舍某區域之正副樓長
                    2. 此區域都為空房
                    若有任何問題，請向開發人員提出。
                    """).queue();
            return;
        }
        
        if (startRoom.isEmpty()) {
            event.getHook().sendMessage("""
                    此區點名已完成。
                    """).queue();
            return;
        }

        attendanceEventUtils.sendAttendanceEmbed(event, startRoom);
    }

}
