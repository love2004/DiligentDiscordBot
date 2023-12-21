package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.activity;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.ActivityService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ActivityReleaseButtonEvent extends ListenerAdapter {
    final
    MenuIdSet menuIdSet;
    final
    ButtonIdSet buttonIdSet;
    final
    ActivityService activityService;

    public ActivityReleaseButtonEvent(MenuIdSet menuIdSet, ButtonIdSet buttonIdSet, ActivityService activityService) {
        this.menuIdSet = menuIdSet;
        this.buttonIdSet = buttonIdSet;
        this.activityService = activityService;
    }

    // TODO: 列出所有未過期的活動給予使用者選取
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!buttonIdSet.getGenerateActivity().equalsIgnoreCase(event.getButton().getId())) return;

        event.deferReply().setEphemeral(true).queue();
        event.getHook().sendMessageComponents(
                ActionRow.of(
                        EntitySelectMenu.create(
                                menuIdSet.getActivity(), EntitySelectMenu.SelectTarget.CHANNEL
                        ).build()
                )
        ).setEphemeral(true).queue();
    }
}
