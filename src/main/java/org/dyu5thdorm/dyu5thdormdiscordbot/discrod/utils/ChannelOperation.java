package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:discord.properties")
public class ChannelOperation {
    // admin
    @Value("${component.button.student-info-by-discord}")
    String discordIdButtonId;
    @Value("${component.button.student-info-by-bedId}")
    String bedIdButtonId;
    @Value("${component.button.student-info-by-studentId}")
    String studentIdButtonId;
    @Value("${component.button.student-info-by-name}")
    String nameButtonId;

    // auth
    @Value("${image.auth}")
    String authImagePath;
    @Value("${component.button.auth}")
    String authButtonId;

    // repair
    @Value("${component.button.repair}")
    String repairButtonId;


    public enum Operation {ADMIN_STUDENT_INFO, AUTH, REPAIR}
    Map<Operation, String> operationChannelIdMap;

    @PostConstruct
    void init() {
        operationChannelIdMap = new HashMap<>();
    }

    public void deleteAllMessage(TextChannel textChannel, int limit) {
        textChannel.getHistoryFromBeginning(limit).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );
    }

    @Nullable
    public Message getMessage(TextChannel textChannel, String messageId) {
        return textChannel.retrieveMessageById(messageId)
                .onErrorMap(throwable -> null)
                .complete();
    }

    public void sendOperationMessage(TextChannel textChannel, Operation operation) {
        switch (operation) {
            case ADMIN_STUDENT_INFO -> textChannel.sendMessage(":mag: 查詢住宿生(模糊查詢)")
                    .addActionRow(
                            Button.primary(discordIdButtonId, "以帳號查詢")
                    )
                    .addActionRow(
                            Button.success(bedIdButtonId, "以房號查詢")
                    )
                    .addActionRow(
                            Button.success(studentIdButtonId, "以學號查詢")
                    )
                    .addActionRow(
                            Button.success(nameButtonId, "以姓名查詢")
                    )
                    .queue();
            case AUTH -> textChannel.sendMessage(authImagePath)
                    .setActionRow(
                            Button.primary(authButtonId, "驗證身份")
                    ).queue();
            case REPAIR -> textChannel.sendMessage("各類報修，請按下按鈕開始依操作報修。報修會自動帶入您的個人資料。\n個人區域維修請務必填上可配合維修時間，若未填上，維修大哥聯絡不到人的話將由幹部拿備用鑰匙開門維修。")
                    .setActionRow(
                            Button.success(repairButtonId, "各類報修")
                                    .withEmoji(Emoji.fromUnicode("U+1F9F0"))
                    ).queue();
        }
    }
}
