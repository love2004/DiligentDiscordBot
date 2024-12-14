package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.menu.AuthMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.modals.AuthModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.AuthErrorType;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnAuthButtonInteractionEvent extends ListenerAdapter {
    final
    AuthModal authModal;
    final
    DiscordLinkService discordLinkService;
    final
    ButtonIdSet buttonIdSet;
    final
    Maintenance maintenance;
    final AuthMenu authMenu;

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAuth().equalsIgnoreCase(eventButtonId)) return;
        String userId = event.getUser().getId();
        if (discordLinkService.isLinked(userId)) {
            event.deferReply().setEphemeral(true).queue();
            event.getHook().sendMessage(
                    AuthErrorType.Linked.getMsg()
            ).setEphemeral(true).queue();
            return;
        }

        event.reply("""
                        請選擇驗證方式
                        > Please choose a verification method
                        """)
                .addComponents(
                        ActionRow.of(
                                authMenu.getSelectMenu()
                        )
                )
                .setEphemeral(true)
                .queue();
    }
}
