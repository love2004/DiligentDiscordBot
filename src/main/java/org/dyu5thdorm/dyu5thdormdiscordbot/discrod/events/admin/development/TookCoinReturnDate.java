package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
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
    @Value("${date.format}")
    String dateFormat;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    DiscordLinkService discordLinkService;
    @Autowired
    ChannelOperation channelOperation;
    @Autowired
    RoleOperation roleOperation;
    DateTimeFormatter formatter;

    @PostConstruct
    void init() {
        formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

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
        ann(event.getJDA(), returnDate);
        event.getHook().sendMessage("登記成功！").setEphemeral(true).queue();
    }

    LocalDate toLocalDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        return LocalDate.of(year, month, day);
    }

    void ann(JDA jda, LocalDate localDate) {
        for (TookCoin tookCoin : tookCoinService.findNotGetBack()) {
            var discordLink = discordLinkService.findByStudentId(tookCoin.getStudent().getStudentId());
            var livingRecord = livingRecordService.findLivingRecordByDiscordId(discordLink.getDiscordId());
            int floor = roleOperation.getFloorByBedId(livingRecord.getBed().getBedId());
            String floorChannelId = channelOperation.getFloorChannelByFloor(floor);
            TextChannel floorChannel = jda.getTextChannelById(floorChannelId);
            if (floorChannel == null) continue;
            floorChannel.sendMessage(
                    String.format(
                            """
                            <@%s>
                            您的吃錢登記的金額廠商已退費，請找時間於管理室領取。
                            **領取期限：即日起至 __%s__ 前**
                            """,
                            discordLink.getDiscordId(),
                            localDate.plusDays(7).format(formatter)
                    )
            ).queue();
        }
    }
}
