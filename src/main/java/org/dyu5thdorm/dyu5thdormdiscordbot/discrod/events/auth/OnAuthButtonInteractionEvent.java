package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.modals.AuthModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnAuthButtonInteractionEvent extends ListenerAdapter {
    final AuthModal authModal;
    final DiscordLinkService discordLinkService;
    final
    ButtonIdSet buttonIdSet;
    @Autowired
    Maintenance maintenance;

    public OnAuthButtonInteractionEvent(AuthModal authModal, DiscordLinkService discordLinkService, ButtonIdSet buttonIdSet) {
        this.authModal = authModal;
        this.discordLinkService = discordLinkService;
        this.buttonIdSet = buttonIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if (buttonId == null || !buttonId.equals(buttonIdSet.getAuth())) return;

        String userId = event.getUser().getId();
        if (discordLinkService.isLinked(userId)) {
            event.reply("您已完成驗證，無需再次提交驗證請求。").setEphemeral(true).queue();
            return;
        }

        event.replyModal(authModal.getModal()).queue();
    }
}
