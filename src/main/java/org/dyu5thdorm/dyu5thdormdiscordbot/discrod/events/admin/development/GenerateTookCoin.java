package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateTookCoin extends ListenerAdapter {
    final ButtonIdSet buttonIdSet;
    final ChannelIdSet channelIdSet;
    final ChannelOperation channelOperation;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!buttonIdSet.getGenerateReqTookCoin().equalsIgnoreCase(event.getButton().getId())) return;

        event.deferReply().setEphemeral(true).queue();

        Optional<TextChannel> channelOptional = Optional.ofNullable(
                event.getJDA().getTextChannelById(channelIdSet.getTookCoin())
        );
        if (channelOptional.isEmpty()) {
            log.error("找不到吃錢登記頻道");
            return;
        }

        TextChannel tookCoinChannel = channelOptional.get();
        channelOperation.deleteAllMessage(tookCoinChannel, 100);

        tookCoinChannel.sendMessage(
                String.format("""
                        # 機器吃錢登記說明
                        - 適用機器：業勤學舍內之洗、烘衣機、販賣機
                        - 說明：
                          - 請於事件發生後一天內登記，逾時不接受申請
                          - 接受申請退費金額為新臺幣 1 ~ 99 元
                          - 登記後再次點擊以下「吃錢登記」按鈕可查詢或簽收登記之款項明細
                          - 登記之款項若未退回，則顯示無法簽收狀態，**__若要取消或是更改內容，請與開發人員聯絡__**
                          - 有多筆吃錢登記記錄者，可於款項全部退回後使用「合併簽收」功能一併簽收
                        - 領取：
                          - 時間：學期內 **__每個禮拜四__**，若時間有更改，皆會在 <#%s> 發布說明
                          - 辦法： **自收到來自 <#%s> 提及之通知後七天(含)內領取退款，__逾時款項將會在學期末全數捐出，不得有異議__**
                          - 方式：__攜帶手機__與 <@%s> 約時間於管理室前領取
                        ### **請注意！販賣機非兌幣機。請妥善使用，切勿僅為換取零錢。如因換零錢遭遇問題，恕不退費！**
                        """, channelIdSet.getAnnouncement(), channelIdSet.getPublicChannel(), "1127821867026219008"
                )
        ).addActionRow(
                Button.primary(buttonIdSet.getTookCoin(), "吃錢登記")
                        .withEmoji(Emoji.fromUnicode("U+1F4B8"))
        ).queue();

        event.getHook().sendMessage("DONE").setEphemeral(true).queue();
    }
}
