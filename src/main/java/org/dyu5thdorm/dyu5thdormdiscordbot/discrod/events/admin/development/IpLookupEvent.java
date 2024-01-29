package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.IpLookup;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IpLookupEvent extends ListenerAdapter {
    final IpLookup ipLookup;
    final Maintenance maintenance;
    final ButtonIdSet buttonIdSet;

    @SneakyThrows
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventBtnId = event.getButton().getId();
        if (!buttonIdSet.getIpLookup().equalsIgnoreCase(eventBtnId)) return;

        event.deferReply().setEphemeral(true).queue();

        if (event.getMember() == null) return;
        if (maintenance.isNotDeveloper(event.getMember())) return;

        event.getHook().sendMessage(String.format("""
                ||%s||
                """, ipLookup.getIp())).setEphemeral(true).queue();
    }
}
