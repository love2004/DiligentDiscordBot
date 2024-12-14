package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.modals.AuthModal;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OnAuthMenuEvent extends ListenerAdapter {
    final MenuIdSet menuIdSet;
    final ModalIdSet modalIdSet;
    final AuthModal authModal;

    @Override
    public void onGenericSelectMenuInteraction(GenericSelectMenuInteractionEvent event) {
        super.onGenericSelectMenuInteraction(event);
        String menuId = event.getSelectMenu().getId();

        if (!menuIdSet.getAuth().equalsIgnoreCase(menuId)) {
            return;
        }

        List<String> selectedOptions = event.getInteraction().getValues();
        if (selectedOptions.isEmpty()) {
            // TODO: UNKOWN ERROR
            return;
        }

        String selected = selectedOptions.get(0);
        if (modalIdSet.getFirstTextInput().equalsIgnoreCase(selected)) {
            event.replyModal(
                    authModal.getPhoneModal()
            ).queue();
        } else {
            event.replyModal(
                    authModal.getMailModal()
            ).queue();
        }
    }
}