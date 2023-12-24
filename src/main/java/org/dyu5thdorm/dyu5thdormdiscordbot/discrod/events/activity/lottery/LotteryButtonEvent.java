package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.activity.lottery;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class LotteryButtonEvent extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    LotteryModel lotteryModel;
    final
    ChannelIdSet channelIdSet;

    public LotteryButtonEvent(ButtonIdSet buttonIdSet, LotteryModel lotteryModel, ChannelIdSet channelIdSet) {
        this.buttonIdSet = buttonIdSet;
        this.lotteryModel = lotteryModel;
        this.channelIdSet = channelIdSet;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!buttonIdSet.getLottery().equalsIgnoreCase(event.getButton().getId())) return;
        event.deferReply().setEphemeral(true).queue();
        TextChannel channel = event.getJDA().getTextChannelById(channelIdSet.getLottery());
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
        event.getHook().sendMessage("ok").setEphemeral(true).queue();
    }
}
