package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class OnReadyEvent extends ListenerAdapter {
    final
    Maintenance maintenance;

    public OnReadyEvent(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        maintenance.update(event.getJDA());
    }
}
