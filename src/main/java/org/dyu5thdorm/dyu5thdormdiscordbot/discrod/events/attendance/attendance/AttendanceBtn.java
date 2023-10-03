package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.stereotype.Component;

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

    public AttendanceBtn(ButtonIdSet buttonIdSet, AttendanceEmbedBuilder attendanceEmbedBuilder, LivingRecordService livingRecordService, AttendanceHandler attendanceHandler) {
        this.buttonIdSet = buttonIdSet;
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.livingRecordService = livingRecordService;
        this.attendanceHandler = attendanceHandler;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendance().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        // 取得當前房間
        var startRoom = attendanceHandler.getRoomStart(event.getUser().getId());
        
        if (startRoom.isEmpty()) {
            event.getHook().sendMessage("""
                    有以下二個原因導致您無法使用此功能：
                    1. 您非宿舍某區域之正副樓長
                    2. 此區域都為空房
                    若有任何問題，請向開發人員提出。
                    """).queue();
            return;
        }

        boolean isEmptyRoom = attendanceHandler.isEmptyRoom(startRoom);
        event.getHook().sendMessageEmbeds(attendanceEmbedBuilder.getByLivingRecord(
                startRoom
        ).build()).addActionRow(
                Button.secondary(buttonIdSet.getAttendancePrev(), "上一房"),
                Button.success(buttonIdSet.getAttendanceAllIn(), "全到").withDisabled(isEmptyRoom),
                Button.primary(buttonIdSet.getAttendanceAllOut(), "全缺").withDisabled(isEmptyRoom),
                Button.danger(buttonIdSet.getAttendanceOut(), "缺").withDisabled(isEmptyRoom),
                Button.secondary(buttonIdSet.getAttendanceNext(), "下一房")
        ).setEphemeral(true).queue();
    }

}
