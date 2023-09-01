package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.embed.TookCoinEmbed;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TookMoneySearch extends ListenerAdapter {
    @Autowired
    MenuIdSet menuIdSet;
    @Autowired
    TookCoin tookCoin;
    @Autowired
    TookCoinEmbed tookCoinEmbed;


    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String eventMenuId = event.getSelectMenu().getId();
        if (!menuIdSet.getTookCoin().equalsIgnoreCase(eventMenuId)) return;
        if (!event.getSelectedOptions().get(0).getValue().equalsIgnoreCase(menuIdSet.getTookCoinSearch())) return;

        var r = tookCoin.getRecordByDiscordId(event.getUser().getId());

        if (r == null || r.isEmpty()) {
            event.reply("查無紀錄").setEphemeral(true).queue();
            return;
        }


        for (org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin coin : r) {
            event.getHook().sendMessageEmbeds(tookCoinEmbed.getBySearchResult(coin).build()).setEphemeral(true).queue();
        }

        event.reply("查詢成功").setEphemeral(true).queue();
    }


}
