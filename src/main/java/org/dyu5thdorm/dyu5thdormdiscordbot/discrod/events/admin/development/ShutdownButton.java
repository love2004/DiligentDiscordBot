package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.Dyu5thDormDiscordBotApplication;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShutdownButton extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    Maintenance maintenance;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!buttonIdSet.getShutdown().equalsIgnoreCase(event.getButton().getId())) return;
        event.deferReply().setEphemeral(true).queue();
        if (maintenance.isNotDeveloper(event.getMember())) {
            event.reply("您無權限操作！").setEphemeral(true).queue();
            return;
        }

        event.getJDA().shutdown();
        SpringApplication.exit(Dyu5thDormDiscordBotApplication.context, () -> 0);
    }
}
