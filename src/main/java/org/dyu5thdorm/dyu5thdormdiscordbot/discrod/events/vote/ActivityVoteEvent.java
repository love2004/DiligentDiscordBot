package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.vote;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.ActivityService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.ParticipantService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ActivityVoteEvent extends ListenerAdapter {
    final ActivityService activityService;
    final DiscordLinkService dcService;
    final LivingRecordService lrService;
    final ParticipantService partiService;
    final ButtonIdSet buttonIdSet;

    Map<String, ParticipantService.Status> idList;

    @PostConstruct
    void postConstruct() {
        idList = Map.of(
                buttonIdSet.getAgree(), ParticipantService.Status.AGREE,
                buttonIdSet.getDisagree(), ParticipantService.Status.DISAGREE,
                buttonIdSet.getAbstention(), ParticipantService.Status.ABSTENTION
        );
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!idList.containsKey(event.getButton().getId())) return;

        event.deferReply().setEphemeral(true).queue();

        ParticipantService.Status status = idList.get(
                event.getButton().getId()
        );

        Optional<LivingRecord> optional = lrService.findLivingRecordByDiscordId(
                event.getUser().getId()
        );
        if (optional.isEmpty()) {
            event.getHook().sendMessage("非本學期住宿生無法進行投票。").setEphemeral(true).queue();
            return;
        }

        if (!activityService.wasStart(2)) {
            event.getHook().sendMessage("投票尚未開始，無法進行投票。").setEphemeral(true).queue();
            return;
        }

        if (activityService.overDeadline(2)) {
            event.getHook().sendMessage("投票已截止，無法再進行投票。").setEphemeral(true).queue();
            return;
        }

        LivingRecord livingRecord = optional.get();
        partiService.save(2, livingRecord.getStudent().getStudentId(), status);

        Integer agreeCount = getCount(ParticipantService.Status.AGREE);
        Integer disagreeCount = getCount(ParticipantService.Status.DISAGREE);
        Integer abstentionCount = getCount(ParticipantService.Status.ABSTENTION);
        List<LayoutComponent> components = event.getMessage().getComponents();

        components.get(0).updateComponent(buttonIdSet.getAgree(), Button.success(buttonIdSet.getAgree(), String.format(
                "同意(%d)", agreeCount
        )));
        components.get(0).updateComponent(buttonIdSet.getDisagree(), Button.danger(buttonIdSet.getDisagree(), String.format(
                "不同意(%d)", disagreeCount
        )));
        components.get(0).updateComponent(buttonIdSet.getAbstention(), Button.secondary(buttonIdSet.getAbstention(), String.format(
                "無意見(%d)", abstentionCount
        )));

        event.getMessage().editMessageComponents(components).queue();
        event.getHook().sendMessage("""
                投票成功！
                > 您仍可於投票截止前更改投票內容
                """
        ).setEphemeral(true).queue();
    }

    Integer getCount(ParticipantService.Status status) {
        return partiService.getParticipantTotal(2, status.getValue());
    }
}
