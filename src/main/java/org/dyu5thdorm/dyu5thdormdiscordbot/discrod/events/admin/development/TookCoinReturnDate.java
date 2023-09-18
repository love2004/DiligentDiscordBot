package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.embed.TookCoinEmbed;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.modals.TookCoinReturnDateModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ChannelOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.RoleOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TookCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TookCoinReturnDate extends ListenerAdapter {
    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    ModalIdSet modalIdSet;
    @Autowired
    TookCoinReturnDateModal tookCoinReturnDateModal;
    @Autowired
    TookCoinService tookCoinService;
    @Value("${regexp.date_year_month_day}")
    String dateYearMonthDayRegexp;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    DiscordLinkService discordLinkService;
    @Autowired
    ChannelOperation channelOperation;
    @Autowired
    RoleOperation roleOperation;
    @Autowired
    TookCoinEmbed tookCoinEmbed;
    DateTimeFormatter formatter;

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
    }

    LocalDate toLocalDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        return LocalDate.of(year, month, day);
    }

    void mentionVictim(JDA jda, LocalDate returnDate) {
        List<String> sent = new ArrayList<>();
        for (TookCoin tookCoin : tookCoinService.findNotGetBack()) {
            var discordLink = discordLinkService.findByStudentId(
                    tookCoin.getStudent().getStudentId()
            );
            if (sent.contains(discordLink.getDiscordId())) continue;
            sent.add(discordLink.getDiscordId());
            var livingRecord = livingRecordService.findLivingRecordByDiscordId(discordLink.getDiscordId());
            int floor;
            String floorChannelId;
            if (livingRecord == null) {
                floorChannelId = channelOperation.getChannelIdSet().getPublicChannel();
            } else {
                floor = roleOperation.getFloorByBedId(livingRecord.getBed().getBedId());
                floorChannelId = channelOperation.getFloorChannelByFloor(floor);
            }
            TextChannel floorChannel = jda.getTextChannelById(floorChannelId);
            if (floorChannel == null) continue;
            EmbedBuilder mentionEmbed = tookCoinEmbed.getMentionGetCoinMessage(returnDate);
            String sentMessageId = floorChannel.sendMessageEmbeds(mentionEmbed.build()).complete().getId();
            floorChannel.sendMessage(String.format(
                    "<@%s>", discordLink.getDiscordId()
            )).setMessageReference(sentMessageId).queue();
        }
    }
}
