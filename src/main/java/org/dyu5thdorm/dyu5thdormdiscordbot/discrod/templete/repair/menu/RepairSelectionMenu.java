package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.menu;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RepairSelectionMenu {
    StringSelectMenu menu;
    @Value("${component.menu.repair}")
    String id;
    @Value("${component.menu.repair-civil}")
    String civilId; // 土木
    @Value("${component.menu.repair-hydro}")
    String hydroId; // 水電
    @Value("${component.menu.repair-door}")
    String doorId; // 門窗鎖具
    @Value("${component.menu.repair-air_cond}")
    String airCondId; // 空調
    @Value("${component.menu.repair-other}")
    String otherId; // 其他
    @Value("${component.menu.repair-wash_and_dry}")
    String washAndDryId; // 洗烘衣機
    @Value("${component.menu.repair-vending}")
    String vendingId; // 販賣機
    @Value("${component.menu.repair-drinking}")
    String drinkingId; // 飲水機

    @PostConstruct
    void init() {
        menu = StringSelectMenu
                .create(id)
                .addOption("土木", civilId, "牆壁、床、地板等", Emoji.fromUnicode("U+1F3D7"))
                .addOption("水電", hydroId, "燈類、插座、衛浴設備等", Emoji.fromUnicode("U+1F50C"))
                .addOption("門窗", doorId, "門、窗、鎖具等", Emoji.fromUnicode("U+1F6AA"))
                .addOption("空調", airCondId, "冷氣", Emoji.fromUnicode("U+2744"))
                .addOption("洗、烘衣機", washAndDryId, "各樓層洗衣機、烘衣機", Emoji.fromUnicode("U+1F9FA"))
                .addOption("販賣機", vendingId, "各樓層販賣機", Emoji.fromUnicode("U+1F4B0"))
                .addOption("飲水機", drinkingId, "各樓層販賣機", Emoji.fromUnicode("U+1F6B0"))
                .addOption("其他", otherId, "其他類別", Emoji.fromUnicode("U+1F6E0"))
                .build();
    }
}
