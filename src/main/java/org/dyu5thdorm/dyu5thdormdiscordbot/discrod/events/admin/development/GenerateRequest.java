package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ReqLevOperation;
import org.springframework.stereotype.Component;

@Component
public class GenerateRequest extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;
    final
    ReqLevOperation rlOp;

    public GenerateRequest(ButtonIdSet buttonIdSet, ChannelIdSet channelIdSet, ReqLevOperation rlOp) {
        this.buttonIdSet = buttonIdSet;
        this.channelIdSet = channelIdSet;
        this.rlOp = rlOp;
    }


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equalsIgnoreCase(buttonIdSet.getGenerateReqLev())) return;
        event.deferReply().setEphemeral(true).queue();

        TextChannel reqLevChannel = event.getJDA().getTextChannelById(channelIdSet.getReqLev());

        if (reqLevChannel == null) {
            return;
        }

        reqLevChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        reqLevChannel.sendMessage(
                String.format("""
                        # 晚間點名請假
                        - 點名時段說明：
                          - **除寒、暑假、國定假日、一般假日(六日)、放假前一天外，上課日皆需點名。**
                          - 點名於 22:30 開始
                        - 點名模式：
                          - 樓長至房間內點名
                        - 注意事項：
                          - 無法到場點名者須依規定請假。
                          - 連續兩天點名缺席未請假者，將會通知家長。
                          - __當天點名請假時間為 **%d0:%d0 ~ %d:%d**，愈時系統不受理！__
                          - 請點下方「晚間點名請假」開始進行請假流程。
                        """, rlOp.getStartLeaveTimeHour(), rlOp.getStartLeaveTimeMin(),
                                rlOp.getEndLeaveTimeHour(), rlOp.getEndLeaveTimeMin())
                )
                .addActionRow(
                        Button.danger(buttonIdSet.getReqForLeave(), "晚間點名請假")
        ).queue();

        event.getHook().sendMessage("Done").setEphemeral(true).queue();
    }
}
