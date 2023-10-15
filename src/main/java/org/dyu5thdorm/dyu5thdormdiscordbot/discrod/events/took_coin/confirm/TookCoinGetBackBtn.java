package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin.confirm;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.embed.TookCoinEmbed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TookCoinService;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;

@Component
public class TookCoinGetBackBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    TookCoinService tookCoinService;
    final
    ChannelIdSet channelIdSet;
    final
    TookCoinEmbed tookCoinEmbed;

    public TookCoinGetBackBtn(ButtonIdSet buttonIdSet, TookCoinService tookCoinService, ChannelIdSet channelIdSet, TookCoinEmbed tookCoinEmbed) {
        this.buttonIdSet = buttonIdSet;
        this.tookCoinService = tookCoinService;
        this.channelIdSet = channelIdSet;
        this.tookCoinEmbed = tookCoinEmbed;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getTookCoinGetBack().equalsIgnoreCase(eventButtonId) &&
                !buttonIdSet.getTookCoinGetBackConfirm().equalsIgnoreCase(eventButtonId)
        ) return;

        event.deferReply().setEphemeral(true).queue();

        var embeds = event.getMessage().getEmbeds();
        if (embeds.isEmpty() || embeds.get(0).getFooter() == null) return;
        Long embedId = Long.parseLong(embeds.get(0).getFooter().getText());
        TookCoin query = tookCoinService.findByRecordId(embedId);
        if (query == null) {
            event.getHook().sendMessage("查無結果").setEphemeral(true).queue();
            return;
        }
        if (query.getGetBackTime() != null) {
            event.getHook().sendMessage("無效的操作！您已簽收過了！").setEphemeral(true).queue();
            return;
        }

        if (query.getReturnDate() == null) {
            event.getHook().sendMessage("錢尚未向廠商請款！無法簽收！").setEphemeral(true).queue();
            return;
        }

        if (buttonIdSet.getTookCoinGetBack().equalsIgnoreCase(eventButtonId)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.ORANGE)
                    .setTitle("確認簽收？")
                    .addField("簽收總額", query.getCoinAmount().toString(), true)
                    .setFooter(query.getId().toString());

            event.getHook().sendMessageEmbeds(embedBuilder.build())
                    .addActionRow(
                            Button.danger(buttonIdSet.getTookCoinGetBackConfirm(), "確認簽收")
                    ).setEphemeral(true).queue();
            return;
        }

        query.setGetBackTime(LocalDateTime.now());
        tookCoinService.save(query);
        TextChannel cadreGetBack = event.getJDA().getTextChannelById(channelIdSet.getTookCoinGetBackCadre());
        if (cadreGetBack == null) return;
        cadreGetBack.sendMessageEmbeds(
                tookCoinEmbed.getBackCadreMessage(query, event.getUser().getId()).build()
        ).queue();
        event.getHook().sendMessage("簽收成功！").setEphemeral(true).queue();
    }
}
