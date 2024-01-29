package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.modals.RepairModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnRepairMenuEvent extends ListenerAdapter {
    final
    MenuIdSet menuIdSet;
    final
    RepairModal repairModal;
    final
    Repair repair;

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String eventMenuId = event.getSelectMenu().getId();
        if (eventMenuId == null || !event.getSelectMenu().getId().equalsIgnoreCase(menuIdSet.getRepair())) return;

        String selectedId = event.getSelectedOptions().get(0) == null ? "" :
                event.getSelectedOptions().get(0).getValue();

        event.replyModal(
                repairModal.getModal(
                        repair.getTypeByMenuId(selectedId)
                )
        ).queue();
    }
}
