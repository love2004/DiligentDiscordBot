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

import java.util.Set;

@Component
public class AttendanceNextBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;

    final
    AttendanceEventUtils attendanceEventUtils;

    public AttendanceNextBtn(ButtonIdSet buttonIdSet, AttendanceHandler attendanceHandler, AttendanceEmbedBuilder attendanceEmbedBuilder, AttendanceEventUtils attendanceEventUtils) {
        this.buttonIdSet = buttonIdSet;
        this.attendanceHandler = attendanceHandler;
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.attendanceEventUtils = attendanceEventUtils;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendanceNext().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        if (event.getMessage().getEmbeds().isEmpty()) {
            event.getHook().sendMessage("NULL").setEphemeral(true).queue();
            return;
        }

        MessageEmbed eventMessageEmbed = event.getMessage().getEmbeds().get(0);
        if (eventMessageEmbed.getFooter() == null) {
            event.getHook().sendMessage("無法查詢").setEphemeral(true).queue();
            return;
        }
        String currentRoomId = eventMessageEmbed.getFooter().getText();
        Set<LivingRecord> next = attendanceHandler.nextRoom(currentRoomId);
        if (next.isEmpty()) {
            event.getHook().sendMessage("已是最後").setEphemeral(true).queue();
            return;
        }
        attendanceEventUtils.sendAttendanceEmbed(event, next);
    }
}
