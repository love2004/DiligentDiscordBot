package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class TookCoinService {
    final
    TookCoinRepository tookCoinRepository;

    public TookCoinService(TookCoinRepository tookCoinRepository) {
        this.tookCoinRepository = tookCoinRepository;
    }


    public void save(TookCoin tookCoinModel) {
        tookCoinRepository.save(tookCoinModel);
    }

    public boolean existsByTimeAndStudentId(LocalDateTime time, String studentId) {
        return tookCoinRepository.existsByTimeAndStudent_StudentId(time, studentId);
    }

    public Set<TookCoin> findByStudentId(String id) {
        return tookCoinRepository.findAllByStudentStudentId(id);
    }
}
