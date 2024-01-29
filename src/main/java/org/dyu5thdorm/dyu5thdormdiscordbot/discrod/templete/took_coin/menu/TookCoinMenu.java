package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.menu;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class TookCoinMenu {
    StringSelectMenu menu;

    final MenuIdSet menuIdSet;

    @PostConstruct
    void init() {
        menu = StringSelectMenu.create(menuIdSet.getTookCoin())
                .setPlaceholder("請選擇要登記吃錢的機器")
                .addOption("販賣機", menuIdSet.getVendingOption(),"各樓層販賣機" , Emoji.fromUnicode("U+1F4B0"))
                .addOption("洗衣機", menuIdSet.getWashingMachineOption(), "各樓層洗衣機", Emoji.fromUnicode("U+1F9FA"))
                .addOption("烘衣機", menuIdSet.getDryerOption(), "各樓層烘衣機", Emoji.fromUnicode("U+2600"))
                .addOption("登記紀錄查詢、簽收", menuIdSet.getTookCoinSearch(), "僅供查詢未退回的紀錄、簽收領回退費功能", Emoji.fromUnicode("U+1F50D"))
                .build();
    }
}
