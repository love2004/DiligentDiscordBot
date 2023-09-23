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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Component
public class TookCoinGetAllBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    TookCoinService tookCoinService;
    @Value("${regexp.took-coin.merge}")
    String tookCoinMergeRegex;
    final
    TookCoinEmbed tookCoinEmbed;
    final
    ChannelIdSet channelIdSet;

    public TookCoinGetAllBtn(ButtonIdSet buttonIdSet, TookCoinService tookCoinService, TookCoinEmbed tookCoinEmbed, ChannelIdSet channelIdSet) {
        this.buttonIdSet = buttonIdSet;
        this.tookCoinService = tookCoinService;
        this.tookCoinEmbed = tookCoinEmbed;
        this.channelIdSet = channelIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getTookCoinGetBackMerge().equalsIgnoreCase(eventButtonId) &&
            !buttonIdSet.getTookCoinGetBackMergeConfirm().equalsIgnoreCase(eventButtonId)
        ) return;

        event.deferReply().setEphemeral(true).queue();
        var embeds = event.getMessage().getEmbeds();
        if (embeds.isEmpty() || embeds.get(0).getFooter() == null) return;
        String footer = embeds.get(0).getFooter().getText();
        List<Long> ids = recordIdFilter(footer);

        if (ids == null) {
            event.getHook().sendMessage("無法合併簽收，因為您已經簽收過了！").setEphemeral(true).queue();
            return;
        }

        List<TookCoin> queryList = ids.stream().map(
                e -> tookCoinService.findByRecordId(e)
        ).toList();

        if (buttonIdSet.getTookCoinGetBackMerge().equalsIgnoreCase(eventButtonId)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            int sum = queryList.stream().mapToInt(e -> {
                if (e.getIsGetBack()) return 0;
                return e.getCoinAmount();
            }).sum();

            if (sum == 0) {
                event.getHook().sendMessage("無法合併簽收，因為您已經簽收過了！").setEphemeral(true).queue();
                return;
            }

            embedBuilder.setTitle("確定合併簽收？")
                    .addField("合併簽收總額", Integer.toString(sum), true)
                    .setColor(Color.ORANGE)
                    .setFooter(
                            queryList.stream().filter(e -> !e.getIsGetBack()).map(
                                    TookCoin::getId
                            ).toList().toString()
                    );
            event.getHook().sendMessageEmbeds(embedBuilder.build())
                    .addActionRow(
                            Button.danger(buttonIdSet.getTookCoinGetBackMergeConfirm(), "確認合併簽收")
                    )
                    .setEphemeral(true).queue();
            return;
        }

        for (TookCoin query : queryList) {
            if (query.getIsGetBack()) {
                event.getHook().sendMessage("無法合併簽收，因為您已經簽收過了！").setEphemeral(true).queue();
                return;
            }
            query.setIsGetBack(Boolean.TRUE);
            tookCoinService.save(query);
        }

        event.getHook().sendMessage( "合併簽收成功").setEphemeral(true).queue();
        TextChannel cadreGetBack = event.getJDA().getTextChannelById(channelIdSet.getTookCoinGetBackCadre());
        if (cadreGetBack == null) return;
        cadreGetBack.sendMessageEmbeds(
                tookCoinEmbed.getBackCadreMessage(queryList, event.getUser().getId()).build()
        ).queue();
    }

    List<Long> recordIdFilter(String footer) {
        if (!footer.matches(tookCoinMergeRegex)) return null;
        return Arrays.stream(footer.replace("[", "").replace("]", "")
                .split(",")).map(
                e -> Long.parseLong(e.trim())
        ).toList();
    }
}
