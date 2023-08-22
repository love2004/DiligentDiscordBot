package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnReadyEvent extends ListenerAdapter {
    @Autowired
    Maintenance maintenance;

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        maintenance.update();
    }
}
