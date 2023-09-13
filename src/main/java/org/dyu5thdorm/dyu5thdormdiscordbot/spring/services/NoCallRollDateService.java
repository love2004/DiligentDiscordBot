package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.NoCallRollDate;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.NoCallRollDateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NoCallRollDateService {
    final
    NoCallRollDateRepository repository;

    public NoCallRollDateService(NoCallRollDateRepository repository) {
        this.repository = repository;
    }

    public boolean exists(LocalDate localDate) {
        return repository.existsByDate(localDate);
    }

    public boolean save(NoCallRollDate noCallRollDate) {
        if (this.exists(noCallRollDate.getDate())) return false;
        repository.save(noCallRollDate);
        return true;
    }
}
