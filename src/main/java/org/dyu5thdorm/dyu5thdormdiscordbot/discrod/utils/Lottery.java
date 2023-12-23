package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.view.TicketView;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class Lottery {

    public List<TicketView> drawWinners(List<TicketView> ticketViews, int numberOfWinners) {
        if (numberOfWinners <= 0) {
            throw new IllegalArgumentException("Number of winners must be greater than 0");
        }

        List<TicketView> tickets = populateTickets(ticketViews);
        Set<TicketView> winners = new HashSet<>();

        Random random = new Random();
        while (winners.size() < numberOfWinners) {
            if (tickets.isEmpty()) {
                throw new IllegalStateException("Not enough tickets to draw the required number of winners");
            }
            TicketView winner = tickets.get(random.nextInt(tickets.size()));
            winners.add(winner);
        }

        return new ArrayList<>(winners);
    }

    private List<TicketView> populateTickets(List<TicketView> ticketViews) {
        List<TicketView> tickets = new ArrayList<>();
        for (TicketView ticketView : ticketViews) {
            int ticketCount = ticketView.getTicketCount().intValue();
            for (int i = 0; i < ticketCount; i++) {
                tickets.add(ticketView);
            }
        }
        Collections.shuffle(tickets);
        return tickets;
    }
}
