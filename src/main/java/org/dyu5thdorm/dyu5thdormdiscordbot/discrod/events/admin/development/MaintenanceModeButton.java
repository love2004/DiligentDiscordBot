package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceModeButton extends ListenerAdapter {
    @Value("${component.button.maintenance}")
    String maintainButtonId;
    @Autowired
    Maintenance maintenance;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (eventButtonId == null || !eventButtonId.equalsIgnoreCase(maintainButtonId)) return;



        boolean current = maintenance.isMaintenanceStatus();
        maintenance.setMaintenanceStatus(!current);
    }
}
