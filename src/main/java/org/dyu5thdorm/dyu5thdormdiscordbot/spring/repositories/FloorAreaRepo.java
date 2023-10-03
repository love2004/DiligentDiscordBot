package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorAreaId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FloorAreaRepo extends JpaRepository<FloorArea, FloorAreaId> {
    Optional<FloorArea> findByFloorEqualsAndAreaIdEquals(@NotNull Integer floor,@NotNull String areaId);
    @Query("SELECT fa from FloorArea fa where fa.floor = :floor and :roomId >= fa.startRoomId and :roomId <= fa.endRoomId")
    Optional<FloorArea> findByFloorAndRoomId(Integer floor, Integer roomId);

    boolean existsByFloorAndEndRoomId(Integer floor, Integer endRoomId);
}
