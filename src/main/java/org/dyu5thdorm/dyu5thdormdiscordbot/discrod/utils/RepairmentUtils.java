package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RepairmentUtils {
    final ChannelIdSet channelIdSet;

    public RepairmentUtils(ChannelIdSet channelIdSet) {
        this.channelIdSet = channelIdSet;
    }

    public MessageEmbed notificationEmbed(RepairModel model) throws Exception {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("%s 提交了報修單".formatted(model.getReporter().getStudentId()));
        builder.setColor(0xFFFF00);
        builder.setDescription("> 報修資訊如下所示：");

        builder.addField("報修區域", model.getLocation(), true);
        builder.addField("損壞敘述", model.getDescription(), true);

        if (model.getUnit() == RepairModel.Unit.Normal) {
            builder.addField("報修物品", model.getItem(), true);
            builder.addField("維修可配合時間", model.getRepairTime() == null ? "無" : model.getRepairTime(), true);
        }

        builder.addField("報修者電話", model.getReporter().getPhoneNumber(), true);

        builder.setFooter("報修時間");
        builder.setTimestamp(new Date().toInstant());
        return builder.build();
    }
}
