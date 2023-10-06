package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Component
public class AttendanceEventUtils {
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;
    final
    AttendanceHandler attendanceHandler;
    @Value("${attendance.start.time.hour}")
    Integer startTimeHour;
    @Value("${attendance.start.time.min}")
    Integer startTimeMin;
    @Value("${attendance.end.time.hour}")
    Integer endTimeHour;
    @Value("${attendance.end.time.min}")
    Integer endTimeMin;
    LocalTime startLocalTime;
    LocalTime endLocalTime;

    @PostConstruct
    void init() {
        startLocalTime = LocalTime.of(startTimeHour, startTimeMin);
        endLocalTime = LocalTime.of(endTimeHour, endTimeMin);
    }

    public AttendanceEventUtils(AttendanceEmbedBuilder attendanceEmbedBuilder, AttendanceHandler attendanceHandler) {
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.attendanceHandler = attendanceHandler;
    }

    public String getRoomIdFromMessage(Message message) {
        if (message.getEmbeds().isEmpty()) return null;
        Optional<MessageEmbed> embed = message.getEmbeds().stream().findFirst();
        if (embed.isEmpty()) return null;
        var footer = embed.get().getFooter();
        return footer == null ? null : footer.getText();
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

    public void sendStartTime(ButtonInteractionEvent event) {
        event.getHook().sendMessage(
                String.format(
                        """
                        > 點名時間尚未開放。
                        > 點名開放時間為每日 %d:%d0 至 %d:%d0
                        """, startTimeHour, startTimeMin, endTimeHour, endTimeMin
                )
        ).setEphemeral(true).queue();
    }

    public void sendEndTime(ButtonInteractionEvent event, boolean reply) {
        String content = String.format(
                """
                > 點名時間已結束。
                > 點名開放時間為每日 %d:%d0 至 %d:%d0
                """, startTimeHour, startTimeMin, endTimeHour, endTimeMin
        );

        if (reply) {
          event.reply(content).setEphemeral(true).queue();
        } else {
            event.getHook().sendMessage(
                    content
            ).setEphemeral(true).queue();
        }
    }
}
