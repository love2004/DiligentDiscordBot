package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.FloorAreaRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FloorAreaService {
    final
    FloorAreaRepo floorAreaRepo;

    public Optional<FloorArea> findByFloorArea(@NotNull FloorArea floorArea) {
        return floorAreaRepo.findByFloorEqualsAndAreaIdEquals(floorArea.getFloor(), floorArea.getAreaId());
    }

    public Optional<FloorArea> findByFloorAndRoomId(Integer floor, Integer roomId) {
        return floorAreaRepo.findByFloorAndRoomId(floor, roomId);
    }

    public boolean isEndByRoomId(Integer floor, Integer roomId) {
        return floorAreaRepo.existsByFloorAndEndRoomId(floor, roomId);
    }
}
