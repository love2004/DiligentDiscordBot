package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GenerateAuth extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ChannelIdSet channelIdSet;
    final
    ChannelOperation channelOperation;
    @Value("${school_year}")
    String schoolYear;
    @Value("${semester}")
    String semester;

    public GenerateAuth(ButtonIdSet buttonIdSet, ChannelIdSet channelIdSet, ChannelOperation channelOperation) {
        this.buttonIdSet = buttonIdSet;
        this.channelIdSet = channelIdSet;
        this.channelOperation = channelOperation;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getGenerateReqAuth().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(channelIdSet.getAuth());
        channelOperation.deleteAllMessage(
                textChannel,100
        );

        textChannel.sendMessage(
                    String.format(
                            """
                            @everyone
                            # %s-%s 業勤住宿生身分驗證說明
    
                            如果您還能看到此頻道，代表您尚未驗證住宿生身份。
                            > If you can still see this channel, it means you have not yet verified your resident identity.
                            若未驗證，你將無法查看本伺服器內的所有內容。
                            > If not verified, you will not be able to access all the content within this server.
                            如何驗證？點擊下方「驗證身分」按鈕。
                            > How to verify? Click the 'Verify Identity' button below.
                            """, schoolYear, semester
                    )
                )
                    .setActionRow(
                            Button.primary(buttonIdSet.getAuth(), "驗證身分")
                    ).queue();

        event.getHook().sendMessage("DONE").setEphemeral(true).queue();
    }
}
