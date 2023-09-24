package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TookCoinService {
    final
    TookCoinRepository tookCoinRepository;

    public TookCoinService(TookCoinRepository tookCoinRepository) {
        this.tookCoinRepository = tookCoinRepository;
    }


    public void save(TookCoin tookCoinModel) {
        if (tookCoinModel.getIsGetBack()) {
            tookCoinModel.setGetBackTime(
                    LocalDateTime.now()
            );
        }
        tookCoinRepository.save(tookCoinModel);
    }

    public boolean existsByTimeAndStudentId(LocalDateTime time, String studentId) {
        return tookCoinRepository.existsByEventTimeAndStudent_StudentId(time, studentId);
    }

    public Set<TookCoin> findByStudentId(String id) {
        return tookCoinRepository.findAllByStudentStudentId(id);
    }

    public Set<TookCoin> findUnGetByStudentId(String id) {
        return tookCoinRepository.findAllByStudentStudentIdAndIsGetBack(id, false);
    }

    public TookCoin findByRecordId(Long id) {
        return tookCoinRepository.findById(id).orElse(null);
    }

    public void saveReturnCoinDay(LocalDate localDate) {
        var r = tookCoinRepository.findAllByDateAndNotReturn(localDate);
        for (TookCoin tookCoin : r) {
            tookCoin.setIsReturn(Boolean.TRUE);
            tookCoin.setReturnTime(localDate);
            tookCoinRepository.save(tookCoin);
        }
    }

    public Set<TookCoin> findNotGetBack() {
        return tookCoinRepository.findAllByIsGetBackAndIsReturn(false, true);
    }

    public Set<TookCoin> findNotGetBack(LocalDate returnDate, int dayIn) {
        return tookCoinRepository.findAllByIsGetBackAndIsReturn(false, true).stream().filter(
                e -> {
                    if (e.getReturnTime() == null) return false;
                    return e.getReturnTime().plusDays(dayIn).isAfter(returnDate) ||
                            e.getReturnTime().plusDays(dayIn).isEqual(returnDate);
                }
        ).collect(Collectors.toSet());
    }
}
