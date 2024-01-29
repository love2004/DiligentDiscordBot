package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateAttendance extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;
    final
    ChannelOperation channelOperation;

    @Value("${attendance.start.time.hour}")
    String startAttendTimeHour;
    @Value("${attendance.start.time.min}")
    String startAttendTimeMin;
    @Value("${attendance.end.time.hour}")
    String endAttendTimeHour;
    @Value("${attendance.end.time.min}")
    String endAttendTimeMin;


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getGenerateReqLevCadre().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(
                channelIdSet.getReqLevCadre()
        );

        if (textChannel == null) {
            event.getHook().sendMessage("頻道不存在").queue();
            return;
        }

        channelOperation.deleteAllMessage(textChannel, 100);

        textChannel.sendMessage(
                        String.format(
                                """
                                        # 幹部點名系統說明：
                                        - 開放時間： %s:%s ~ %s:%s0
                                          - 請於開放時間內完成點名
                                        - 點名完成後，若有住宿生要補點，請叫他直接去請假
                                        - 點名務必確實，人對床位
                                        - 若點名系統出包，請立刻反應並先換回紙本方式點名
                                        - 點名系統說明：[hackmd.io/@NUTT1101/discord](https://hackmd.io/@NUTT1101/discord)
                                                                
                                        點擊下方 **「開始點名」** 即可開始
                                        """, startAttendTimeHour, startAttendTimeMin, endAttendTimeHour, endAttendTimeMin
                        )
                ).addActionRow(
                        Button.primary(buttonIdSet.getAttendance(), "開始點名")
                )
                .queue();
        event.getHook().sendMessage("DONE").queue();
    }
}
