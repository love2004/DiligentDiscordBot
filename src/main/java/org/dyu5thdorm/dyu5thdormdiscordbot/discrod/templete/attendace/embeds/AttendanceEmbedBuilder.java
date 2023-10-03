package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Component
public class AttendanceEmbedBuilder {
    @Value("${regexp.bed_id}")
    String bedIdRegex;
    final
    ButtonIdSet buttonIdSet;

    public AttendanceEmbedBuilder(ButtonIdSet buttonIdSet) {
        this.buttonIdSet = buttonIdSet;
    }

    public EmbedBuilder getByLivingRecord(List<LivingRecord> livingRecords) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String roomId;
        if (!isSameRoom(livingRecords)) {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("錯誤");
            return embedBuilder;
        }
        roomId = getRoomByGetId(livingRecords.get(0).getBed().getBedId());
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setTitle(roomId);

        for (LivingRecord livingRecord : livingRecords) {
            if (livingRecord.getStudent() == null) {
                embedBuilder.addField(
                        livingRecord.getBed().getBedId(), "> 空床", false
                );
            } else {
                embedBuilder.addField(
                        livingRecord.getBed().getBedId(),
                        String.format(
                                """
                                > %s
                                > %s
                                > %s
                                > %s
                                """,
                                livingRecord.getStudent().getStudentId(),
                                livingRecord.getStudent().getName(),
                                livingRecord.getStudent().getMajor(),
                                livingRecord.getStudent().getCitizenship()
                        ), false);
            }
        }
        embedBuilder.setFooter(roomId);
        return embedBuilder;
    }

    public EmbedBuilder getByLivingRecord(Set<LivingRecord> livingRecords) {
        return getByLivingRecord(livingRecords.stream().toList());
    }

    boolean isSameRoom(List<LivingRecord> records) {
        if (records.isEmpty()) return false;
        String roomId = getRoomByGetId(records.get(0).getBed().getBedId());
        if (roomId == null) return false;
        return records.stream().allMatch(e -> e.getBed().getBedId().contains(roomId));
    }

    String getRoomByGetId(String bedId) {
        if (!bedId.matches(bedIdRegex)) return null;
        return bedId.substring(0, 4);
    }

    public List<ItemComponent> getAttendanceActionRow(boolean isLastRoom, boolean isEmptyRoom) {
         return List.of(
                 Button.secondary(buttonIdSet.getAttendancePrev(), "上一房"),
                 Button.success(buttonIdSet.getAttendanceAllIn(), "全到").withDisabled(isEmptyRoom),
                 Button.primary(buttonIdSet.getAttendanceAllOut(), "全缺").withDisabled(isEmptyRoom),
                 Button.danger(buttonIdSet.getAttendanceOut(), "缺").withDisabled(isEmptyRoom),
                 isLastRoom ? Button.success(buttonIdSet.getAttendanceComplete(), "點名完成") :
                         Button.secondary(buttonIdSet.getAttendanceNext(), "下一房")
         );
    }
}
