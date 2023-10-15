package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
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

    public AttendanceEventUtils(AttendanceEmbedBuilder attendanceEmbedBuilder, AttendanceHandler attendanceHandler) {
        this.attendanceEmbedBuilder = attendanceEmbedBuilder;
        this.attendanceHandler = attendanceHandler;
    }

    @PostConstruct
    void init() {
        startLocalTime = LocalTime.of(startTimeHour, startTimeMin);
        endLocalTime = LocalTime.of(endTimeHour, endTimeMin);
    }

    public String getRoomIdFromMessage(Message message) {
        if (message.getEmbeds().isEmpty()) return null;
        Optional<MessageEmbed> embed = message.getEmbeds().stream().findFirst();
        if (embed.isEmpty()) return null;
        var footer = embed.get().getFooter();
        return footer == null ? null : footer.getText();
    }

    public <T> void sendAttendanceEmbed(T event, Set<LivingRecord> roomSet) {
        if (!(event instanceof ButtonInteractionEvent || event instanceof ModalInteractionEvent)) return;
        MessageEmbed roomEmbed = attendanceEmbedBuilder.getByLivingRecord(roomSet).build();
        if (roomEmbed.getFooter() == null) {
            if (event instanceof ButtonInteractionEvent buttonEvent) {
                buttonEvent.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            } else {
                ModalInteractionEvent modalEvent = (ModalInteractionEvent) event;
                modalEvent.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            }
            return;
        }

        boolean isEmptyRoom = attendanceHandler.isEmptyRoom(roomSet);
        boolean isLastRoom = attendanceHandler.isLastRoom(roomEmbed.getFooter().getText());
        if (event instanceof ButtonInteractionEvent buttonEvent) {
            buttonEvent.getHook().sendMessageEmbeds(roomEmbed)
                    .setActionRow(attendanceEmbedBuilder.getAttendanceActionRow(isLastRoom, isEmptyRoom))
                    .queue();
        } else {
            ModalInteractionEvent modalEvent = (ModalInteractionEvent) event;
            modalEvent.getHook().sendMessageEmbeds(roomEmbed)
                    .setActionRow(attendanceEmbedBuilder.getAttendanceActionRow(isLastRoom, isEmptyRoom))
                    .queue();
        }
    }

    public <T> void showNextRoom(T event, Set<LivingRecord> next) {
        if (!(event instanceof ButtonInteractionEvent || event instanceof ModalInteractionEvent)) return;
        if (event instanceof ButtonInteractionEvent buttonEvent) {
            if (next == null) {
                buttonEvent.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
                return;
            }
            if (next.isEmpty()) {
                buttonEvent.getHook().sendMessageEmbeds(attendanceEmbedBuilder.complete().build()).setEphemeral(true).queue();
                return;
            }

        } else {
            ModalInteractionEvent modalEvent = (ModalInteractionEvent) event;
            if (next == null) {
                modalEvent.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
                return;
            }
            if (next.isEmpty()) {
                modalEvent.getHook().sendMessageEmbeds(attendanceEmbedBuilder.complete().build()).setEphemeral(true).queue();
                return;
            }
        }

        sendAttendanceEmbed(event, next);
    }

    public void sendStartTime(ButtonInteractionEvent event) {
        event.getHook().sendMessage(
                String.format(
                        """
                        > 點名時間尚未開放。
                        > 點名開放時間為上課日 %d:%d 至 %d:%d0
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
