package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChannelOperation {
    @Autowired
    ChannelIdSet channelIdSet;
    Map<Integer, String > floorChannelMap;

    @PostConstruct
    void init() {
        floorChannelMap = new HashMap<>();
        floorChannelMap.put(1, channelIdSet.getFloorOne());
        floorChannelMap.put(2, channelIdSet.getFloorTwo());
        floorChannelMap.put(3, channelIdSet.getFloorThree());
        floorChannelMap.put(4, channelIdSet.getFloorFour());
        floorChannelMap.put(5, channelIdSet.getFloorFive());
        floorChannelMap.put(6, channelIdSet.getFloorThree());
    }

    public void deleteAllMessage(TextChannel textChannel, int limit) {
        if (textChannel == null) {
            return;
        }

        textChannel.getHistoryFromBeginning(limit).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );
    }

    public String  getFloorChannelByFloor(Integer floor) {
        return floorChannelMap.getOrDefault(floor, null);
    }

//    public void sendOperationMessage(TextChannel textChannel, Operation operation) {
//        switch (operation) {
//            case ADMIN_STUDENT_INFO -> textChannel.sendMessage(":mag: 查詢住宿生(模糊查詢)")
//                    .addActionRow(
//                            Button.primary(discordIdButtonId, "以帳號查詢")
//                    )
//                    .addActionRow(
//                            Button.success(bedIdButtonId, "以房號查詢")
//                    )
//                    .addActionRow(
//                            Button.success(studentIdButtonId, "以學號查詢")
//                    )
//                    .addActionRow(
//                            Button.success(nameButtonId, "以姓名查詢")
//                    )
//                    .queue();
//            case AUTH -> textChannel.sendMessage("# 身分驗證說明\n\n### 為驗證您的住宿生身份，請照以下步驟驗證：\n- 點擊下方 **驗證身分** 按鈕\n- 依提示操作開始驗證身分")
//                    .setActionRow(
//                            Button.primary(authButtonId, "驗證身分")
//                    ).queue();
//            case REPAIR -> textChannel.sendMessage("# 各類報修(水電土木、洗、烘衣機、販賣機、飲水機)\n\n- 報修會自動帶入您的個人資料，無需額外填寫。\n- 個人區域維修**請務必填上可配合維修時間**\n - 若未填上，將配合維修人員將配合值班幹部領備用鑰匙進入維修。")
//                    .addActionRow(
//                            Button.success(repairButtonId, "各類報修")
//                                    .withEmoji(Emoji.fromUnicode("U+1F9F0"))
//                    ).addActionRow(
//                            Button.primary("todo", "吃錢登記")
//                                    .withEmoji(Emoji.fromUnicode("U+1F4B0"))
//                    )
//                    .queue();
//        }
//    }
}
