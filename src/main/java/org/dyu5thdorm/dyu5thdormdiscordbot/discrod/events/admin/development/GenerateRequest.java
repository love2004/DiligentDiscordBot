package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.stereotype.Component;

@Component
public class GenerateRequest extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;

    public GenerateRequest(ButtonIdSet buttonIdSet, ChannelIdSet channelIdSet) {
        this.buttonIdSet = buttonIdSet;
        this.channelIdSet = channelIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equalsIgnoreCase(buttonIdSet.getGenerateReqLev())) return;

        TextChannel reqLevChannel = event.getJDA().getTextChannelById(channelIdSet.getReqLev());

        if (reqLevChannel == null) {
            return;
        }

        reqLevChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        reqLevChannel.sendMessage("""
                        # 晚間點名請假 (Evening Roll Call Leave)
                        - 點名時段說明 (Roll Call Period Explanation)：
                          - **除寒、暑假、國定假日、放假前一天外，上課日皆需點名。**
                          - 點名於 22:30 開始
                          >  **Except for winter and summer breaks, national holidays, and the day before holidays, roll call is required on school days.**
                        - 點名模式 (Roll Call Method)：
                          - 樓長至房間內點名
                          > Floor RA(Resident Advisor) will conduct roll call inside the rooms.
                        
                        無法到場點名者須依規定請假。
                        > Those unable to attend roll call must follow the regulations and request leave.
                        
                        連續兩天點名缺席未請假者，將會通知家長。
                        > Students who are absent from roll call for two consecutive days without requesting leave will have their parents notified.
                        
                        __點名請假時間為 **23:00** 前，愈時系統不受理！__
                        > __The deadline for requesting leave for evening roll call **on the same day is 23:00**; requests made after this time will not be accepted by the system!__
                        
                        請點下方「晚間點名請假 Request」開始進行請假流程。
                        > "Please click '晚間點名請假 Request' below to begin the leave request process."
                        """)
                .addActionRow(
                        Button.danger(buttonIdSet.getReqForLeave(), "晚間點名請假 Request")
        ).queue();

        event.reply("Done").setEphemeral(true).queue();
    }
}
