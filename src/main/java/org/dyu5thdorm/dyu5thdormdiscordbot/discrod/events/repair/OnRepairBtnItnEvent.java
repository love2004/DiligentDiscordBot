package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.menu.RepairSelectionMenu;
import org.springframework.stereotype.Component;

@Component
public class OnRepairBtnItnEvent extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    RepairSelectionMenu selectionMenu;

    public OnRepairBtnItnEvent(ButtonIdSet buttonIdSet, RepairSelectionMenu selectionMenu) {
        this.buttonIdSet = buttonIdSet;
        this.selectionMenu = selectionMenu;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventBtnId = event.getButton().getId();
        if (eventBtnId == null || !eventBtnId.equalsIgnoreCase(buttonIdSet.getRepair())) return;
        event.deferReply().setEphemeral(true).queue();

        event.getHook().sendMessageComponents(
                ActionRow.of(
                        selectionMenu.getMenu()
                )
        ).setEphemeral(true).queue();
    }
}
