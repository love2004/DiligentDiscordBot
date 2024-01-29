package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.ActivityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenerateVote extends ListenerAdapter {
    final ButtonIdSet buttonIdSet;
    final ChannelIdSet channelIdSet;
    final ActivityService activityService;
    final ChannelOperation channelOperation;

    @Value("${datetime.format}")
    String dateTimeFormat;
    String format;

    @PostConstruct
    void postConstruct() {
        format = """
            # %s
            - 開始時間：%s
            - 截止時間：%s
            - 結束時間：%s
            ### 說明：
            %s
            """;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!buttonIdSet.getGenerateVote().equalsIgnoreCase(event.getButton().getId())) return;

        event.deferReply().setEphemeral(true).queue();

        Optional<TextChannel> channelOptional = Optional.ofNullable(
                event.getJDA().getTextChannelById(channelIdSet.getVote())
        );

        if (channelOptional.isEmpty()) {
            return;
        }

        Optional<Activity> activities = activityService.findActivity(2);
        if (activities.isEmpty()) {
            return;
        }
        Activity activity = activities.get();

        TextChannel channel = channelOptional.get();

        channelOperation.deleteAllMessage(channel, 100);

        channel.sendMessage(
                        String.format(
                                this.format,
                                activity.getActivityName(),
                                DateTimeFormatter.ofPattern(dateTimeFormat).format(activity.getStartTime()),
                                DateTimeFormatter.ofPattern(dateTimeFormat).format(activity.getRegistrationDeadline()),
                                DateTimeFormatter.ofPattern(dateTimeFormat).format(activity.getEndTime()),
                                activity.getActivityDescription()
                        )
                )
                .addComponents(
                        ActionRow.of(
                                Button.success(buttonIdSet.getAgree(), "同意"),
                                Button.danger(buttonIdSet.getDisagree(), "不同意"),
                                Button.secondary(buttonIdSet.getAbstention(), "無意見")
                        )
                )
                .queue();
        event.getHook().sendMessage("OK").setEphemeral(true).queue();
    }
}
