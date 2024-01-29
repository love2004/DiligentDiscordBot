package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnReadyEvent extends ListenerAdapter {
    final
    Maintenance maintenance;

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        maintenance.update(event.getJDA());
    }
}
