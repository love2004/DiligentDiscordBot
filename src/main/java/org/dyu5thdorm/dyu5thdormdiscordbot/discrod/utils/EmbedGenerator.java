package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@PropertySource("classpath:discord.properties")
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
                .setThumbnail(
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
}
