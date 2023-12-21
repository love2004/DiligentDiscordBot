package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.view.TicketView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketViewRepo extends JpaRepository<TicketView, String> {
    @Query("SELECT SUM(t.ticketCount) FROM TicketView t")
    Double findTotalAmount();
}
