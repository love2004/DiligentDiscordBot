package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.embeds.AuthEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.AuthErrorType;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.AuthUtils;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OnAuthModalInteractionEvent extends ListenerAdapter {
    final
    LivingRecordService livingRecordService;
    final
    DiscordLinkService discordLinkService;
    final
    StudentService studentService;
    final
    AuthEmbedBuilder authEmbedBuilder;
    final
    ModalIdSet modalIdSet;
    final
    ChannelIdSet channelIdSet;
    final AuthUtils authUtils;

    List<String> modalIdList;

    @PostConstruct
    void setup() {
        this.modalIdList = List.of(
                modalIdSet.getAuthPhone(),
                modalIdSet.getAuthMail()
        );
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String modalId = event.getModalId();
        if (!this.modalIdList.contains(
                modalId
        )) return;

        String userId = event.getUser().getId();

        String studentId = event.getValue(modalIdSet.getFirstTextInput()).getAsString().toUpperCase();
        String secondField = event.getValue(modalIdSet.getSecondTextInput()).getAsString();

        AuthErrorType type;
        if (this.modalIdList.get(0).equalsIgnoreCase(modalId)) {
            type = authUtils.authByPhone(
                    event.getMember(),
                    studentId,
                    secondField
            );
        } else {
            type = authUtils.authMail(
                    event.getMember(),
                    studentId,
                    secondField
            );
        }

        event.reply(
                type.getMsg()
        ).setEphemeral(true).queue();

        if (type != AuthErrorType.NONE) {
            return;
        }

        Optional<LivingRecord> livingRecord = livingRecordService.findByStudentId(studentId);

        EmbedBuilder embedBuilder = authEmbedBuilder.successAuth(livingRecord.orElse(null));
        event.getHook().sendMessageEmbeds(
                embedBuilder.build()
        ).setEphemeral(true).queue();
        TextChannel logger = event.getJDA().getTextChannelById(channelIdSet.getAuthLogger());
        if (logger != null) {
            logger.sendMessageEmbeds(
                    authEmbedBuilder.successAuthLogger(embedBuilder, userId).build()
            ).queue();
        }
    }

}
