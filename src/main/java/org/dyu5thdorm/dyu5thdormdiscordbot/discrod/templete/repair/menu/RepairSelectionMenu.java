package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.menu;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.springframework.stereotype.Component;

@Component
@Data
public class RepairSelectionMenu {
    StringSelectMenu menu;
    final
    MenuIdSet menuIdSet;

    public RepairSelectionMenu(MenuIdSet menuIdSet) {
        this.menuIdSet = menuIdSet;
    }

    @PostConstruct
    void init() {
        menu = StringSelectMenu
                .create(menuIdSet.getRepair())
                .setPlaceholder("請選擇要報修的類別")
                .addOption("土木 Civil", menuIdSet.getCivilOption(), "牆壁、床、地板等", Emoji.fromUnicode("U+1F3D7"))
                .addOption("水電 Plumbing and Electrical", menuIdSet.getHydroOption(), "燈類、插座、衛浴設備等", Emoji.fromUnicode("U+1F50C"))
                .addOption("門窗 Doors, Windows, and Locks", menuIdSet.getDoorOption(), "門、窗、鎖具等", Emoji.fromUnicode("U+1F6AA"))
                .addOption("空調 Air conditioner", menuIdSet.getAirCondOption(), "冷氣", Emoji.fromUnicode("U+2744"))
                .addOption("洗、烘衣機 Washing machine or Dryer", menuIdSet.getWashAndDryOption(), "各樓層洗衣機、烘衣機", Emoji.fromUnicode("U+1F9FA"))
                .addOption("販賣機 Vending", menuIdSet.getVendingOption(), "各樓層販賣機", Emoji.fromUnicode("U+1F4B0"))
                .addOption("飲水機 Water dispenser", menuIdSet.getDrinkingOption(), "各樓層飲水機", Emoji.fromUnicode("U+1F6B0"))
                .addOption("其他 Others", menuIdSet.getOtherOption(), "其他類別", Emoji.fromUnicode("U+1F6E0"))
                .build();
    }
}
