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

import java.util.ArrayList;
import java.util.List;

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

    List<EmbedBuilder> getEmbedBuilders() {
        List<TicketView> ticketViews = viewService.getAll();
        List<TicketView> winners = lottery.drawWinners(ticketViews, 3);
        List<EmbedBuilder> builders = new ArrayList<>();
        for (int i = 0; i < winners.size(); i++) {
            DiscordLink discordLink = dcService.findByStudentId(winners.get(i).getStudentId());
            activityPartiService.lotteryWinner(winners.get(i).getStudentId());
            EmbedBuilder embedBuilder = embedGenerator.fromWinners(i, winners.get(i), discordLink);
            builders.add(embedBuilder);
        }
        return builders;
    }
}
