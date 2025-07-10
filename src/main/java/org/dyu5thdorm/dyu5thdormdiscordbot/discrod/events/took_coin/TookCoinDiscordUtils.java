package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.springframework.stereotype.Component;

@Component
public class TookCoinDiscordUtils {
    final ButtonIdSet buttonIdSet;

    public TookCoinDiscordUtils(ButtonIdSet buttonIdSet) {
        this.buttonIdSet = buttonIdSet;
    }

    public void sendReturnDateButton(MessageChannel channel) {
        channel.sendMessageComponents(
                ActionRow.of(
                        Button.danger(buttonIdSet.getTookCoinReturn(), "廠商退幣日期登記")
                )
        ).queue();

    }
}
