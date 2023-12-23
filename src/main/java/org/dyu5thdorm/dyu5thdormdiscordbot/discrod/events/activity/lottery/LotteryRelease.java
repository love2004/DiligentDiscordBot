package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.activity.lottery;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LotteryRelease {
    final
    DiscordAPI dcAPI;
    final
    ChannelIdSet channelIdSet;
    final
    LotteryModel lotteryModel;

    public LotteryRelease(DiscordAPI dcAPI, ChannelIdSet channelIdSet, LotteryModel lotteryModel) {
        this.dcAPI = dcAPI;
        this.channelIdSet = channelIdSet;
        this.lotteryModel = lotteryModel;
    }


    @Scheduled(cron = "0 30 20 27 12 *")
    void release() {
        TextChannel channel = dcAPI.getJda().getTextChannelById(channelIdSet.getLottery());
        channel.getIterableHistory().complete().forEach(message -> message.delete().queue());
        lotteryModel.getEmbedBuilders().forEach(e -> channel.sendMessageEmbeds(e.build()).queue());
        channel.sendMessage("""
                ---
                ### 以上中獎者請於 *2023/12/30 20:29:29* 以前內私訊 <@537977217397817372>，逾期視為放棄。
                ---
                @everyone
                """).queue();
    }
}
