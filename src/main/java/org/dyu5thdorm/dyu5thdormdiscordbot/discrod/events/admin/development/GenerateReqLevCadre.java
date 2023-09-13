package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateReqLevCadre extends ListenerAdapter {
    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    ChannelIdSet channelIdSet;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getGenerateReqLevCadre().equalsIgnoreCase(eventButtonId)) return;

        TextChannel textChannel = event.getJDA().getTextChannelById(
                channelIdSet.getReqLevCadre()
        );

        if (textChannel == null) {
            event.reply("頻道不存在").queue();
            return;
        }

        textChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );

        textChannel.sendMessage("""
                # 幹部 *暫時* 點名系統說明：
                點擊下方按鈕後可得知，今晚申請請假的住宿生人數，請在點名&請假系統尚未完成時，依照以下步驟進行點名：
                - 於每天 22:30 進行實體點名
                - 點名完後點下方按鈕查看今天的請假住宿生資訊
                - 審核請假資訊資訊並 key 入 點名 Google 試算表 (試算表製作完成後會訂選在此頻道)
                
                **注意！請假申請只開放到 23:00，若有住宿生超時仍想請假，請各位樓長依照自己的作息，斟酌申請受理與否！**
                """).addActionRow(
                        Button.primary(buttonIdSet.getReqForLeaveCadre(), "查看請假申請紀錄")
                )
                .queue();
    }
}
