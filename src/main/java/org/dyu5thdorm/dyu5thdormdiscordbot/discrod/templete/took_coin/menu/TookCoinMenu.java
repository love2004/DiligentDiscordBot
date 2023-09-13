package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.menu;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TookCoinMenu {
    StringSelectMenu menu;
    @Autowired
    MenuIdSet menuIdSet;

    @PostConstruct
    void init() {
        menu = StringSelectMenu.create(menuIdSet.getTookCoin())
                .setPlaceholder("請選擇要登記吃錢的機器")
                .addOption("販賣機", menuIdSet.getVendingOption(),"各樓層販賣機" , Emoji.fromUnicode("U+1F4B0"))
                .addOption("洗衣機", menuIdSet.getWashingMachineOption(), "各樓層洗衣機", Emoji.fromUnicode("U+1F9FA"))
                .addOption("烘衣機", menuIdSet.getDryerOption(), "各樓層烘衣機", Emoji.fromUnicode("U+2600"))
                .addOption("吃錢登記紀錄查詢", menuIdSet.getTookCoinSearch(), "僅供查詢，不可修改，只會顯示未退回的紀錄", Emoji.fromUnicode("U+1F50D"))
                .build();
    }
}
