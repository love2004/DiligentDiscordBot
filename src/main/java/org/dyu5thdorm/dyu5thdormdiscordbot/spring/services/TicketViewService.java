package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TicketViewRepo;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.view.TicketView;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketViewService {
    final
    TicketViewRepo ticketViewRepo;

    public TicketViewService(TicketViewRepo ticketViewRepo) {
        this.ticketViewRepo = ticketViewRepo;
    }

    public Double getTotalTicketAmount() {
        return ticketViewRepo.findTotalAmount();
    }

    public Optional<TicketView> getBallAmount(String studentId) {
        return ticketViewRepo.findById(studentId);
    }
}
