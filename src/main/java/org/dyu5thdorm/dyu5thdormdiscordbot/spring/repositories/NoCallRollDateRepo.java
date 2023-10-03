package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.NoCallRollDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface NoCallRollDateRepo extends JpaRepository<NoCallRollDate, Integer> {
    boolean existsByDate(LocalDate date);
}
