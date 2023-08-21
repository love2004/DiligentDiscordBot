package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.menu.RepairSelectionMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OnRepairBtnItnEvent extends ListenerAdapter {
    @Value("${component.button.repair}")
    String repairBtnId;
    @Autowired
    RepairSelectionMenu selectionMenu;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventBtnId = event.getButton().getId();
        if (eventBtnId == null || !eventBtnId.equalsIgnoreCase(repairBtnId)) return;

        event.replyComponents(
                ActionRow.of(
                        selectionMenu.getMenu()
                )
        ).setEphemeral(true).queue();
    }
}
