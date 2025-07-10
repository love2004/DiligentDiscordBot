package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class EmbedGenerator {
    public EmbedBuilder buildLivingInfoEmbed(
            @NotNull LivingRecord livingRecord,
            @Nullable String userId,
            @Nullable DiscordLink discordLink
    ) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("查詢結果")
                .setColor(Color.GREEN)
                .setFooter("查詢成功");

        embedBuilder.setImage(
                String.format("attachment://%s.png", livingRecord.getStudent().getStudentId().toUpperCase())
        );

        if (userId != null) {
            embedBuilder.setDescription(String.format("<@%s> 綁定住宿生身份如下：", userId));
        }

        embedBuilder.clearFields()
                .addField("房號", livingRecord.getBed().getBedId(), true)
                .addField("學號", livingRecord.getStudent().getStudentId(), true)
                .addField("姓名", livingRecord.getStudent().getName(), true)
                .addField("系籍", livingRecord.getStudent().getMajor(), true)
                .addField("國籍", livingRecord.getStudent().getCitizenship(), true)
                .addField("電話",
                        livingRecord.getStudent().getPhoneNumber() != null ?
                                livingRecord.getStudent().getPhoneNumber() :
                                "無資料",
                        true);

        if (discordLink != null) {
            embedBuilder.addField("綁定帳號", String.format("<@%s>", discordLink.getDiscordId()), true);
        }

        return embedBuilder;
    }

    public EmbedBuilder infoFromDiscord(@NotNull LivingRecord livingRecord, String userId) {
        return buildLivingInfoEmbed(livingRecord, userId, null);
    }

    public EmbedBuilder infoFromRoom(@NotNull LivingRecord livingRecord, DiscordLink discordLink) {
        return buildLivingInfoEmbed(livingRecord, null, discordLink);
    }

    public EmbedBuilder infoFromStudentId(LivingRecord livingRecord, DiscordLink discordLink) {
        return buildLivingInfoEmbed(livingRecord, null, discordLink);
    }

    public EmbedBuilder infoFromName(LivingRecord livingRecord, DiscordLink discordLink) {
        return buildLivingInfoEmbed(livingRecord, null, discordLink);
    }
}
