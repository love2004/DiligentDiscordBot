package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateAuth extends ListenerAdapter {
    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    ChannelIdSet channelIdSet;
    @Autowired
    ChannelOperation channelOperation;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getGenerateReqAuth().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(channelIdSet.getAuth());
        channelOperation.deleteAllMessage(
                textChannel,100
        );

        textChannel.sendMessage("""
                        @everyone
                        # 身分驗證說明

                        **如果您還能看到此頻道，代表您尚未驗證住宿生身份。
                        若未驗證，你將無法查看本伺服器的所有內容(包括最新公告、報修、等各項功能)。

                        如何驗證？點擊下方「驗證身分」按鈕。

                        若您非本宿舍112-1學期的 **業勤** 住宿生，請您退出本伺服器，謝謝配合。**
                        ---
                        **If you can still see this channel, it indicates that you haven't verified your status as a resident student yet.
                        Without verification, you won't be able to access all the content in this server, including the latest announcements, repair requests, and other functionalities.

                        How to verify? Click the "驗證身分" button located below.

                        If you are not a resident student for the 112-1 semester in this dormitory, kindly exit this server. Thank you.**

                        ### 為驗證您的住宿生身份，請照以下步驟驗證：
                        - 點擊下方 **驗證身分** 按鈕
                        - 依提示操作開始驗證身分""")
                    .setActionRow(
                            Button.primary(buttonIdSet.getAuth(), "驗證身分")
                    ).queue();

        event.getHook().sendMessage("DONE").setEphemeral(true).queue();
    }
}
