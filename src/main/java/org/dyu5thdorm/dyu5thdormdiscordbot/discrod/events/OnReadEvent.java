package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class OnReadEvent extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }
}
