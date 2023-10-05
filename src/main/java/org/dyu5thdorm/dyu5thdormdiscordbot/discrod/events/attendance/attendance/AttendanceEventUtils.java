package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AttendanceEventUtils {
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;
    final
    AttendanceHandler attendanceHandler;



    public AttendanceEventUtils(AttendanceEmbedBuilder attendanceEmbedBuilder, AttendanceHandler attendanceHandler) {
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.attendanceHandler = attendanceHandler;
    }

    public void sendAttendanceEmbed(ButtonInteractionEvent event, Set<LivingRecord> roomSet) {
        MessageEmbed roomEmbed = attendanceEmbedBuilder.getByLivingRecord(roomSet).build();
        sendIfNull(event, roomEmbed);
        boolean isEmptyRoom = attendanceHandler.isEmptyRoom(roomSet);
        boolean isLastRoom = attendanceHandler.isLastRoom(roomEmbed.getFooter().getText());
        event.getHook().sendMessageEmbeds(
                roomEmbed
        ).setActionRow(
                attendanceEmbedBuilder.getAttendanceActionRow(isLastRoom, isEmptyRoom)
        ).queue();
    }

    public void sendIfNull(ButtonInteractionEvent event, MessageEmbed embed) {
        if (embed.getFooter() == null) {
            event.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
        }
    }
}
