package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.activity;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.ActivityPartiService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TicketViewService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.view.TicketView;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;

@Component
public class ActivityParticipateButtonEvent extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    LivingRecordService lrService;
    final
    ActivityPartiService partiService;
    final
    TicketViewService tvService;

    public ActivityParticipateButtonEvent(ButtonIdSet buttonIdSet, LivingRecordService lrService, ActivityPartiService partiService, TicketViewService bvService) {
        this.buttonIdSet = buttonIdSet;
        this.lrService = lrService;
        this.partiService = partiService;
        this.tvService = bvService;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!buttonIdSet.getActivityParticipate().equalsIgnoreCase(event.getButton().getId())) return;
        event.deferReply().setEphemeral(true).queue();
        LivingRecord recordLinked = lrService.findLivingRecordByDiscordId(event.getUser().getId());
        if (recordLinked == null) {
            event.getHook().sendMessage("非本學期業勤住宿生無法參加此活動！").setEphemeral(true).queue();
            return;
        }

        if (partiService.full()) {
            event.getHook().sendMessage("無法報名！已超過活動報名人數上限！").setEphemeral(true).queue();
            return;
        }

        if (partiService.expired(LocalDateTime.now())) {
            event.getHook().sendMessage("無法報名！已超過活動報名期限！").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embedBuilder;

        boolean isParticipated = partiService.isParticipate(recordLinked.getStudent());

        if (!isParticipated) {
            partiService.participate(recordLinked.getStudent());
            embedBuilder = getInformationEmbed(recordLinked.getStudent());
            embedBuilder.setTitle("登記成功！");
        } else {
            embedBuilder = getInformationEmbed(recordLinked.getStudent());
        }

        event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    public EmbedBuilder getInformationEmbed(@NotNull Student student) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        tvService.getBallAmount(student.getStudentId())
                .ifPresentOrElse(
                        ticketView -> buildInformationEmbed(embedBuilder, ticketView),
                        () -> buildNoDataEmbed(embedBuilder)
                );

        return embedBuilder;
    }

    private void buildInformationEmbed(@NotNull EmbedBuilder embedBuilder, TicketView ticketView) {
        Integer partiTotal = partiService.getParticipantCount();
        Double tickets = ticketView.getTicketCount();
        Double ticketTotal = tvService.getTotalTicketAmount();
        Double attendanceRate = ticketView.getAttendanceRate();
        Double rate = (tickets / ticketTotal) * 100;

        embedBuilder.setTitle("活動-個人資訊");
        embedBuilder.setDescription("以下資訊會隨著參加人數多寡變動：");
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.addField("抽獎參加總人數", String.format("%d 人", partiTotal), true);
        embedBuilder.addField("個人擁有券數", String.format("%d 張", tickets.intValue()), true);
        embedBuilder.addField("抽獎箱總券數", String.format("%d 張", ticketTotal.intValue()), true);
        embedBuilder.addField("晚間點名出席率", String.format("%.2f%%", attendanceRate * 100), true);
        embedBuilder.addField("個人中獎機率", String.format("%.2f%%", rate), true);
    }

    private void buildNoDataEmbed(@NotNull EmbedBuilder embedBuilder) {
        embedBuilder.setTitle("查無登記資料");
        embedBuilder.setDescription("請聯絡主辦人員！");
        embedBuilder.setColor(Color.RED);
    }
}
