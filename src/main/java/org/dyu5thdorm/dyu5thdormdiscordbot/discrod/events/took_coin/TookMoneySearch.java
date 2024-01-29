package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.embed.TookCoinEmbed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoinHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TookMoneySearch extends ListenerAdapter {
    final
    MenuIdSet menuIdSet;
    final
    TookCoinHandler tookCoin;
    final
    TookCoinEmbed tookCoinEmbed;
    final
    ButtonIdSet buttonIdSet;

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        String eventMenuId = event.getSelectMenu().getId();
        if (!menuIdSet.getTookCoin().equalsIgnoreCase(eventMenuId)) return;
        if (!event.getSelectedOptions().get(0).getValue().equalsIgnoreCase(menuIdSet.getTookCoinSearch())) return;

        event.deferReply().setEphemeral(true).queue();

        Set<TookCoin> queryRecords = tookCoin.getRecordUnGetByDiscordId(event.getUser().getId());

        if (queryRecords.isEmpty()) {
            event.getHook().sendMessage("查無紀錄").setEphemeral(true).queue();
            return;
        }

        for (org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin record : queryRecords) {
            event.getHook().sendMessageEmbeds(
                    tookCoinEmbed.getBySearchResult(record).build()
            ).addActionRow(
                    record.getReturnDate() != null ?
                            Button.success(buttonIdSet.getTookCoinGetBack(), "簽收") :
                            Button.success(buttonIdSet.getTookCoinGetBack(), "簽收").asDisabled()
            ).setEphemeral(true).queue();
        }

        if (queryRecords.size() > 1) {
            boolean allReturn = queryRecords.stream().allMatch(e -> e.getReturnDate() != null);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            int backCoinSum = queryRecords.stream().mapToInt(TookCoin::getCoinAmount).sum();
            embedBuilder.setTitle("合併簽收")
                    .setDescription("因發現您有多筆吃錢登記紀錄，因此您可以點此按鈕一併領取")
                    .setColor(Color.CYAN)
                    .addField("應退總額", Integer.toString(backCoinSum), true)
                    .setFooter(
                            queryRecords.stream().map(TookCoin::getId).toList().toString()
                    );
            event.getHook().sendMessageEmbeds(
                    embedBuilder.build()
            ).addActionRow(
                    allReturn ?
                    Button.success(buttonIdSet.getTookCoinGetBackMerge(), "合併簽收") :
                            Button.success(buttonIdSet.getTookCoinGetBackMerge(), "合併簽收").asDisabled()
            ).setEphemeral(true).queue();
        }
    }
}
