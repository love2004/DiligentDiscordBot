package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.modals.AuthModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:discord.properties")
public class OnAuthButtonInteractionEvent extends ListenerAdapter {
    @Autowired
    AuthModal authModal;
    @Autowired
    DiscordLinkService discordLinkService;
    @Value("${component.button.auth}")
    String authButtonId;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if (buttonId == null) return;

        if (!buttonId.equals(authButtonId)) return;

        String userId = event.getUser().getId();

        if (discordLinkService.isLinked(userId)) {
            event.reply("您已完成驗證，無需再次提交驗證請求。").setEphemeral(true).queue();
            return;
        }

        event.replyModal(authModal.getModal()).queue();
    }
}
