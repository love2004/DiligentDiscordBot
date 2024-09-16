package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.embed.TookCoinEmbed;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.modals.TookCoinReturnDateModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.RoleOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TookCoinService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TookCoinReturnDate extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    ModalIdSet modalIdSet;
    final
    TookCoinReturnDateModal tookCoinReturnDateModal;
    final
    TookCoinService tookCoinService;
    final
    LivingRecordService livingRecordService;
    final
    DiscordLinkService discordLinkService;
    final
    ChannelOperation channelOperation;
    final
    RoleOperation roleOperation;
    final
    TookCoinEmbed tookCoinEmbed;
    @Value("${regexp.date_year_month_day}")
    String dateYearMonthDayRegexp;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getTookCoinReturn().equalsIgnoreCase(eventButtonId)) return;

        event.replyModal(tookCoinReturnDateModal.getModal()).queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!modalIdSet.getTookCoinReturn().equalsIgnoreCase(eventModalId)) return;

        event.deferReply().setEphemeral(true).queue();

        String date = event.getValues().get(0).getAsString();
        if (!date.matches(dateYearMonthDayRegexp)) {
            event.getHook().sendMessage("日期格式錯誤").setEphemeral(true).queue();
            return;
        }

        LocalDate returnDate = toLocalDate(date);
        tookCoinService.saveReturnCoinDay(returnDate);
        mentionVictim(event.getJDA(), returnDate);

        event.getHook().sendMessage("登記成功！").setEphemeral(true).queue();

        event.getChannel().getIterableHistory().forEach(
                message -> message.delete().complete()
        );

        event.getChannel().sendMessageComponents(
                ActionRow.of(
                        Button.danger(buttonIdSet.getTookCoinReturn(), "廠商退幣日期登記")
                )
        ).queue();
    }


    LocalDate toLocalDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        return LocalDate.of(year, month, day);
    }

    void mentionVictim(JDA jda, LocalDate returnDate) {
        var notGetBackRecord = tookCoinService.findNotGetBack(returnDate, 7);
        if (notGetBackRecord.isEmpty()) return;
        EmbedBuilder mentionEmbed = tookCoinEmbed.getMentionGetCoinMessage(returnDate);
        TextChannel publicChannel = jda.getTextChannelById(
                channelOperation.getChannelIdSet().getPublicChannel()
        );

        if (publicChannel == null) {
            return;
        }

        String messageId = publicChannel.sendMessageEmbeds(mentionEmbed.build()).complete().getId();
        List<String> victimDiscordIdList = notGetBackRecord.stream().map(
                e -> {
                    DiscordLink discordLink = discordLinkService.findByStudentId(
                            e.getStudent().getStudentId()
                    );
                    return String.format(
                            "<@%s>", discordLink.getDiscordId()
                    );
                }
        ).distinct().toList();

        publicChannel.sendMessage(
                String.join(" ", victimDiscordIdList)
        ).setMessageReference(messageId).queue();
    }
}
