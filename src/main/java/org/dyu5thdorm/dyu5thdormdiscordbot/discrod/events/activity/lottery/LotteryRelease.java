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
        channel.sendMessageEmbeds(lotteryModel.getEmbedBuilder().build()).queue();
        channel.sendMessage("""
                ---
                ### 🎉 恭喜
                ### 請於 **2023年12月30日 20:29:29** 前私訊舍長 <@537977217397817372>。
                ### ⏰ 特別提醒，若在上述截止時間後才聯繫，將視同放棄中獎資格。
                ---
                @everyone
                """).queue();
    }
}
