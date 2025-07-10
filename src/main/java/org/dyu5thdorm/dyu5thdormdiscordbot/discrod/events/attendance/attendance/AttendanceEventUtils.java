package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AttendanceEventUtils {
    final
    AttendanceEmbedBuilder attendanceEmbedBuilder;
    final
    AttendanceHandler attendanceHandler;
    final ChannelOperation channelOperation;
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

    public <T> void showNextRoom(T event, Set<LivingRecord> next, String cadreDiscordId) {
        if (!(event instanceof ButtonInteractionEvent || event instanceof ModalInteractionEvent)) return;

        IReplyCallback callbackEvent = (IReplyCallback) event;
        if (next == null) {
            callbackEvent.getHook().sendMessage("錯誤，請聯絡開發人員。").setEphemeral(true).queue();
            return;
        }

        // has next not complete room
        if (!next.isEmpty()) {
            sendAttendanceEmbed(event, next);
            return;
        }

        Optional<FloorArea> optional = attendanceHandler.getFloorByCadreId(cadreDiscordId);

        optional.ifPresent(floorArea -> {
            String floorChannelId = channelOperation.getFloorChannelMap().get(floorArea.getFloor());
            TextChannel floorChanel = callbackEvent.getJDA().getTextChannelById(floorChannelId);

            if (floorChanel != null) {
                floorChanel.sendMessage(String.format(
                    """
                    %d%s點名完畢，點名時不在房間的住宿生，請記得補點名/請假。
                    """, floorArea.getFloor(), floorArea.getAreaId()
                    )
                ).queue();
            }
        });

        callbackEvent.getHook().sendMessageEmbeds(attendanceEmbedBuilder.complete().build()).setEphemeral(true).queue();
    }

    public void sendStartTime(ButtonInteractionEvent event) {
        event.getHook().sendMessage(
                String.format(
                        """
                        > 點名時間尚未開放。
                        > 點名開放時間為上課日 %s 至 %s
                        """, startLocalTime, endLocalTime
                )
        ).setEphemeral(true).queue();
    }

    public void sendEndTime(ButtonInteractionEvent event, boolean reply) {
        String content = String.format(
                """
                > 點名時間已結束。
                > 點名開放時間為每日 %s 至 %s
                """, startLocalTime, endLocalTime
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
