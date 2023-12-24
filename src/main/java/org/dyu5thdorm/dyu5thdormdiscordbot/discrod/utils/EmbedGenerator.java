package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.view.TicketView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class EmbedGenerator {
    @Value("${image.student-api}")
    String formatStudentImageApi;

    public EmbedBuilder fromDiscord(LivingRecord livingRecord, String userId) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("查詢結果");
        embedBuilder.setDescription(
                        String.format("<@%s> 綁定住宿生身份如下：", userId)
                ).setColor(Color.GREEN)
                .setFooter("查詢成功")
                .setImage(
                        String.format(formatStudentImageApi, livingRecord.getStudent().getStudentId().toUpperCase())
                );
        embedBuilder.clearFields();
        embedBuilder
                .addField("房號", livingRecord.getBed().getBedId(), true)
                .addField("學號", livingRecord.getStudent().getStudentId(), true)
                .addField("姓名", livingRecord.getStudent().getName(), true)
                .addField("系籍", livingRecord.getStudent().getMajor(), true)
                .addField("國籍", livingRecord.getStudent().getCitizenship(), true)
                .addField("電話",
                        livingRecord.getStudent().getPhoneNumber() == null ?
                                "無資料" :
                                livingRecord.getStudent().getPhoneNumber()
                        , true);
        return embedBuilder;
    }

    public EmbedBuilder fromRoom(LivingRecord livingRecord, DiscordLink discordLink) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("查詢結果");
        embedBuilder
                .setColor(Color.GREEN)
                .setFooter("查詢成功")
                .setImage(
                        String.format(formatStudentImageApi, livingRecord.getStudent().getStudentId().toUpperCase())
                );
        embedBuilder.clearFields();
        embedBuilder
                .addField("房號", livingRecord.getBed().getBedId(), true)
                .addField("學號", livingRecord.getStudent().getStudentId(), true)
                .addField("姓名", livingRecord.getStudent().getName(), true)
                .addField("系籍", livingRecord.getStudent().getMajor(), true)
                .addField("國籍", livingRecord.getStudent().getCitizenship(), true)
                .addField("電話", livingRecord.getStudent().getPhoneNumber() != null ?
                        livingRecord.getStudent().getPhoneNumber() :
                        "無資料", true)
                .addField("綁定帳號", discordLink != null ?
                        String.format("<@%s>", discordLink.getDiscordId()) :
                        "無", true);
            return embedBuilder;
    }

    public EmbedBuilder fromStudentId(LivingRecord livingRecord, DiscordLink discordLink) {
        return fromRoom(livingRecord, discordLink);
    }

    public EmbedBuilder fromName(LivingRecord livingRecord, DiscordLink discordLink) {
        return fromRoom(livingRecord, discordLink);
    }

    public EmbedBuilder fromWinners(Map<TicketView, DiscordLink> map) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder strBuilder = new StringBuilder();
        embedBuilder.setColor(Color.decode("#FFD700"));
        AtomicInteger count = new AtomicInteger(1);
        embedBuilder.setTitle("中獎資訊如下");
        map.forEach((ticketView, dcLink) -> strBuilder.append(
                String.format(
                        """
                        ### 第 %d 位：%s
                        - 擁有抽獎券 %d 張
                        """, count.getAndAdd(1),
                        dcLink == null ? "學號：" + ticketView.getStudentId() + "(未綁定 Discord)" : "<@" + dcLink.getDiscordId() +">",
                        ticketView.getTicketCount().intValue()
                )));
        embedBuilder.setDescription(strBuilder);
        return embedBuilder;
    }
}
