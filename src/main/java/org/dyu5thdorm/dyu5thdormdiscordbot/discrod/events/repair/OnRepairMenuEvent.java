package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.modals.RepairModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OnRepairMenuEvent extends ListenerAdapter {
    @Value("${component.menu.repair}")
    String menuId;
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

    @Autowired
    RepairModal repairModal;

    @Autowired
    Repair repair;

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String eventMenuId = event.getSelectMenu().getId();
        if (eventMenuId == null || !event.getSelectMenu().getId().equalsIgnoreCase(menuId)) return;

        String selectedId = event.getSelectedOptions().get(0) == null ? "" :
                event.getSelectedOptions().get(0).getValue();

        event.replyModal(
                repairModal.getModal(
                        repair.getTypeByMenuId(selectedId)
                )
        ).queue();
    }
}
