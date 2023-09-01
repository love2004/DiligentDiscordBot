package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceModeButton extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    Maintenance maintenance;

    public MaintenanceModeButton(Maintenance maintenance, ButtonIdSet buttonIdSet) {
        this.maintenance = maintenance;
        this.buttonIdSet = buttonIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (eventButtonId == null || !eventButtonId.equalsIgnoreCase(buttonIdSet.getMaintenance())) return;

        boolean current = maintenance.isMaintenanceStatus();
        maintenance.setMaintenanceStatus(event.getJDA(), !current);
        event.reply(
                maintenance.isMaintenanceStatus() ? "維修模式：開啟" : "維修模式：關閉"
        ).setEphemeral(true).queue();
    }
}
