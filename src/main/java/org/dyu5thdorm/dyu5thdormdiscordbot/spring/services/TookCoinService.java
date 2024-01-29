package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.TookCoinRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TookCoinService {
    final TookCoinRepo tookCoinRepository;

    public void save(TookCoin tookCoinModel) {
        tookCoinRepository.save(tookCoinModel);
    }

    public boolean existsByTimeAndStudentId(LocalDateTime time, String studentId) {
        return tookCoinRepository.existsByEventTimeAndStudent_StudentId(time, studentId);
    }

    public Set<TookCoin> findByStudentId(String id) {
        return tookCoinRepository.findAllByStudentStudentId(id);
    }

    public Set<TookCoin> findUnGetByStudentId(String id) {
        return tookCoinRepository.findAllByStudentStudentIdAndGetBackTimeIsNull(id);
    }

    public TookCoin findByRecordId(Long id) {
        return tookCoinRepository.findById(id).orElse(null);
    }

    public void saveReturnCoinDay(LocalDate localDate) {
        var r = tookCoinRepository.findAllByDateAndNotReturn(localDate);
        for (TookCoin tookCoin : r) {
            tookCoin.setReturnDate(localDate);
            tookCoinRepository.save(tookCoin);
        }
    }

    public Set<TookCoin> findNotGetBack(LocalDate returnDate, int dayIn) {
        return tookCoinRepository.findAllNotGetBack().stream().filter(
                e -> {
                    if (e.getReturnDate() == null) return false;
                    return e.getReturnDate().plusDays(dayIn).isAfter(returnDate) ||
                            e.getReturnDate().plusDays(dayIn).isEqual(returnDate);
                }
        ).collect(Collectors.toSet());
    }
}
