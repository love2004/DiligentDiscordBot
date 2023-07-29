package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.Dyu5thDormDiscordBotApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShutdownButton extends ListenerAdapter {
    @Value("${component.button.shutdown}")
    String shutdownButtonId;
    @Value("${user.developers}")
    List<String> developers;
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals(shutdownButtonId)) return;

        String userId = event.getUser().getId();

        if (!developers.contains(userId)) {
            event.reply("此頻道的功能只有特定人員才可使用，您無權這樣做。").setEphemeral(true).queue();
            return;
        }

        event.reply("關機中...").setEphemeral(true).queue();
        event.getJDA().shutdown();
        SpringApplication.exit(Dyu5thDormDiscordBotApplication.context, () -> 0);
    }
}
