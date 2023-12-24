package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.activity.lottery;

import net.dv8tion.jda.api.EmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.EmbedGenerator;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Lottery;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.ActivityPartiService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TicketViewService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.view.TicketView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LotteryModel {
    final
    Lottery lottery;
    final
    TicketViewService viewService;
    final
    ActivityPartiService activityPartiService;
    final
    DiscordLinkService dcService;
    final
    EmbedGenerator embedGenerator;

    public LotteryModel(Lottery lottery, TicketViewService viewService, ActivityPartiService activityPartiService, DiscordLinkService dcService, EmbedGenerator embedGenerator) {
        this.lottery = lottery;
        this.viewService = viewService;
        this.activityPartiService = activityPartiService;
        this.dcService = dcService;
        this.embedGenerator = embedGenerator;
    }

    EmbedBuilder getEmbedBuilder() {
        List<TicketView> ticketViews = viewService.getAll();
        List<TicketView> winners = lottery.drawWinners(ticketViews, 7);
        activityPartiService.lotteryWinner(winners);
        Map<TicketView, DiscordLink> resultMap = winners.stream().collect(
                        Collectors.toMap(ticketView -> ticketView,ticketView -> dcService.findByStudentId(ticketView.getStudentId()))
                );

        return embedGenerator.fromWinners(resultMap);
    }
}
