package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@PropertySource("classpath:discord.properties")
public class EmbedGenerator {
    @Autowired
    DiligentEmbed diligentEmbed;
    @Value("${image.student-api}")
    String formatStudentImageApi;

    public EmbedBuilder fromDiscord(LivingRecord livingRecord, String userId) {
        diligentEmbed.setTitle("查詢結果");
        diligentEmbed.setDescription(
                        String.format("<@%s> 綁定住宿生身份如下：", userId)
                ).setColor(Color.GREEN)
                .setFooter("查詢成功")
                .setImage(
                        String.format(formatStudentImageApi, livingRecord.getStudent().getStudentId().toUpperCase())
                );
        diligentEmbed.clearFields();
        diligentEmbed
                .addField("房號", livingRecord.getBed().getBedId(), true)
                .addField("學號", livingRecord.getStudent().getStudentId(), true)
                .addField("姓名", livingRecord.getStudent().getName(), true)
                .addField("系籍", livingRecord.getStudent().getMajor(), true)
                .addField("國籍", livingRecord.getStudent().getCitizenship(), true)
                .addField("電話", livingRecord.getStudent().getPhoneNumber(), true);
        return diligentEmbed;
    }

    public EmbedBuilder fromRoom(LivingRecord livingRecord, DiscordLink discordLink) {
        diligentEmbed.setTitle("查詢結果");
        diligentEmbed
                .setColor(Color.GREEN)
                .setFooter("查詢成功")
                .setImage(
                        String.format("http://icare.dyu.edu.tw/tw/edu/dyu/35project_01/case_01/photo_z/%s.jpg", livingRecord.getStudent().getStudentId().toUpperCase())
                );
        diligentEmbed.clearFields();
        diligentEmbed
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
            return diligentEmbed;
    }

    public EmbedBuilder fromStudentId(Student student) {
        return null;
    }
}

@Component
class DiligentEmbed extends EmbedBuilder {
}
