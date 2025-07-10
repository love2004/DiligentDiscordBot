package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.AttendanceDTO;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AttendanceEmbedBuilder {
    @Value("${regexp.bed_id}")
    String bedIdRegex;
    final
    ButtonIdSet buttonIdSet;

    public EmbedBuilder getByLivingRecord(List<LivingRecord> livingRecords) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String roomId;
        if (!this.isSameRoom(livingRecords)) {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("錯誤");
            return embedBuilder;
        }
        roomId = getRoomIdStringByBedId(livingRecords.get(0).getBed().getBedId());
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
        String roomId = getRoomIdStringByBedId(records.get(0).getBed().getBedId());
        if (roomId == null) return false;
        return records.stream().allMatch(e -> e.getBed().getBedId().contains(roomId));
    }

    public String getRoomIdStringByBedId(String bedId) {
        if (!bedId.matches(bedIdRegex)) return null;
        return bedId.substring(0, 4);
    }

    public List<ItemComponent> getAttendanceActionRow(boolean isLastRoom, boolean isEmptyRoom) {
        List<ItemComponent> actionRows = new java.util.ArrayList<>(List.of(
                Button.secondary(buttonIdSet.getAttendancePrev(), "上一房"),
                Button.success(buttonIdSet.getAttendanceAllIn(), "全到").withDisabled(isEmptyRoom),
                Button.primary(buttonIdSet.getAttendanceAllOut(), "全缺").withDisabled(isEmptyRoom),
                Button.danger(buttonIdSet.getAttendanceOut(), "缺").withDisabled(isEmptyRoom)
        ));
        if (!isLastRoom) {
            actionRows.add(
                    Button.secondary(buttonIdSet.getAttendanceNext(), "下一房")
            );
        }
        return actionRows;
    }

    public EmbedBuilder complete() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("點名完成");
        embedBuilder.setDescription("辛苦了！");
        embedBuilder.setImage("https://i.imgur.com/NWyr6vZ.png");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTimestamp(new Date().toInstant());
        return embedBuilder;
    }

    public EmbedBuilder getPersonalCheck(Set<AttendanceDTO> records, LivingRecord livingRecord) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (records.isEmpty()) {
            embedBuilder.setTitle("查無結果");
            embedBuilder.setColor(Color.RED);
            return embedBuilder;
        }

        embedBuilder.setTitle("晚點名點名紀錄查詢");
        embedBuilder.setDescription(
                String.format(
                        """
                        ### 個人資訊：
                        **床號：%s
                        學號：%s
                        姓名：%s**
                        
                        """,
                        livingRecord.getBed().getBedId(),
                        livingRecord.getStudent().getStudentId(),
                        livingRecord.getStudent().getName()
                )
        );
        embedBuilder.setFooter("狀態為缺且有請假資訊者，系統則會判定為假");
        embedBuilder.setTimestamp(new Date().toInstant());
        embedBuilder.setColor(Color.GREEN);
        records.stream().limit(5).forEach(e -> embedBuilder.addField(
                e.getDate().toString(),
                String.format(
                        """
                        > 出席狀態：%s
                        > 點名時間：%s
                        %s
                        """,
                        this.attendanceStatus(
                                e.getStatusId(), e.getLeaveReason()
                        ),
                        e.getTime(),
                        e.getLeaveReason() == null ? "" : "> 請假資訊：" + e.getLeaveReason()
                )
                , false
        ));

        return embedBuilder;
    }

    String attendanceStatus(Integer status, String reason) {
        if (status == 1) {
            return "到";
        } else if (status == 2 && reason != null) {
            return "假";
        }

        return "缺";
    }
}
