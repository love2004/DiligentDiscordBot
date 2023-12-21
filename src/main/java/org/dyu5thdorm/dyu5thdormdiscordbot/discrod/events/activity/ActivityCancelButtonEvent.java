package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.activity;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.ActivityPartiService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ActivityCancelButtonEvent extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ActivityPartiService partiService;
    final
    LivingRecordService lrService;

    public ActivityCancelButtonEvent(ButtonIdSet buttonIdSet, ActivityPartiService partiService, LivingRecordService lrService) {
        this.buttonIdSet = buttonIdSet;
        this.partiService = partiService;
        this.lrService = lrService;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!buttonIdSet.getActivityCancel().equalsIgnoreCase(event.getButton().getId())) return;

        event.deferReply().setEphemeral(true).queue();
        LivingRecord recordLinked = lrService.findLivingRecordByDiscordId(event.getUser().getId());
        if (recordLinked == null) {
            event.getHook().sendMessage("非本學期業勤住宿生無法參加此活動！").setEphemeral(true).queue();
            return;
        }
        if (!partiService.cancel(recordLinked.getStudent())) {
            event.getHook().sendMessage("未報名此活動不可取消").setEphemeral(true).queue();
            return;
        }
        event.getHook().sendMessage("已取消").setEphemeral(true).queue();
    }
}
