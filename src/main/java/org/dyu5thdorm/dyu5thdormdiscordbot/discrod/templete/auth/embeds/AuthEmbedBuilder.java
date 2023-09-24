package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;

@Component
public class AuthEmbedBuilder {
    public EmbedBuilder successAuth(LivingRecord livingRecord) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("住宿資料")
                .setDescription("以下為您的住宿資料，若有誤或非本人請立刻與宿舍幹部反應。")
                .addField("房號", livingRecord.getBed().getBedId(), true)
                .addField("學號", livingRecord.getStudent().getStudentId(), true)
                .addField("姓名", livingRecord.getStudent().getName(), true)
                .setColor(Color.GREEN);
        return embedBuilder;
    }

    public EmbedBuilder successAuthLogger(EmbedBuilder embedBuilder, @NotNull String userId) {
        embedBuilder
                .setDescription(
                        String.format("<@%s> 綁定資訊如下：", userId)
                )
                .setFooter("綁定時間")
                .setTimestamp(
                        OffsetDateTime.now()
                );

        return embedBuilder;
    }
}
