package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GenerateReqLevCadre extends ListenerAdapter {
    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    ChannelIdSet channelIdSet;
    @Value("${available.time.hour}")
    Integer availableTimeHour;
    @Value("${available.time.minute}")
    Integer availableTimeMinute;


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

        textChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        textChannel.sendMessage(
                String.format(
                        """
                        # 幹部點名系統說明：
                        
                        
                        **注意！請假申請只開放到 %d:%d，若有住宿生超時仍想請假，請各位樓長依照自己的作息，斟酌申請受理與否！**
                        """, availableTimeHour, availableTimeMinute
                )
                ).addActionRow(
                        Button.primary(buttonIdSet.getAttendance(), "開始點名")
                )
                .queue();
        event.getHook().sendMessage("DONE").queue();
    }
}
