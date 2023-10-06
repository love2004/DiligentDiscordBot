package org.dyu5thdorm.dyu5thdormdiscordbot.attendance;

import jakarta.annotation.PostConstruct;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre.FloorAreaCadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AttendanceHandler {
    final
    LivingRecordService livingRecordService;
    final
    DiscordLinkService discordLinkService;
    final
    FloorAreaCadreService floorAreaCadreService;
    final
    FloorAreaService floorAreaService;
    final
    AttendanceService attendanceService;
    final
    Maintenance maintenance;

    @Value("${regexp.bed_id}")
    String bedIdRegex;
    @Value("${attendance.start.time.hour}")
    Integer startTimeHour;
    @Value("${attendance.start.time.min}")
    Integer startTimeMin;
    @Value("${attendance.end.time.hour}")
    Integer endTimeHour;
    @Value("${attendance.end.time.min}")
    Integer endTimeMin;
    LocalTime startLocalTime;
    LocalTime endLocalTime;

    @PostConstruct
    void init() {
        startLocalTime = LocalTime.of(startTimeHour, startTimeMin);
        endLocalTime = LocalTime.of(endTimeHour, endTimeMin);
    }

    public AttendanceHandler(LivingRecordService livingRecordService, DiscordLinkService discordLinkService, FloorAreaCadreService floorAreaCadreService, FloorAreaService floorAreaService, AttendanceService attendanceService, Maintenance maintenance) {
        this.livingRecordService = livingRecordService;
        this.discordLinkService = discordLinkService;
        this.floorAreaCadreService = floorAreaCadreService;
        this.floorAreaService = floorAreaService;
        this.attendanceService = attendanceService;
        this.maintenance = maintenance;
    }

    public boolean isLegalTime(@NotNull LocalTime time) {
        return time.isBefore(startLocalTime) && !maintenance.isMaintenanceStatus();
    }

    public boolean isAfter(LocalTime time) {
        boolean isAfterThan = time.isAfter(endLocalTime) || time.equals(endLocalTime);
        return isAfterThan && !maintenance.isMaintenanceStatus();
    }

    public boolean hasComplete(String currentRoomId, @NotNull Set<LivingRecord> next) {
        return next.isEmpty() || isLastRoom(currentRoomId);
    }

    public Set<LivingRecord> nextRoom(String currentRoomId) {
        return roomOperation(currentRoomId, 1);
    }

    public Set<LivingRecord> prevRoom(String currentRoomId) {
        return roomOperation(currentRoomId, -1);
    }

    // 開始點名，依照樓長的區域開始做點名。
    public Set<LivingRecord> getRoomStart(String floorCadreDiscordId) {
        DiscordLink discordLink = discordLinkService.findByDiscordId(floorCadreDiscordId);
        if (discordLink == null) return null;
        Student student = discordLink.getStudent();
        Optional<FloorAreaCadre> optional = getFloorCadre(student);
        if (optional.isEmpty()) return null;
        FloorAreaCadre floorAreaCadre = optional.get();
        var byFloorArea = floorAreaService.findByFloorArea(floorAreaCadre.getFloorArea());
        if (byFloorArea.isEmpty()) {
            return null;
        }
        if (isAllEmptyRoomArea(byFloorArea.get())) {
            return null;
        }

        var notCompleteList = checkNotComplete(floorAreaCadre);
        if (notCompleteList == null || notCompleteList.isEmpty()) {
            return Set.of();
        }

        return notCompleteList;
    }

    Optional<FloorAreaCadre> getFloorCadre(Student student) {
        return floorAreaCadreService.findByCadreStudent(student);
    }

    String getRoomIdString(Integer floor, Integer room) {
        if (floor == null || room == null) return null;
        return "5" + floor + (room < 10 ? "0" + room : room);
    }

    public Set<LivingRecord> roomOperation(String currentRoomId, Integer offset) {
        Integer floor = getFloorByRoomString(currentRoomId);
        Integer roomId = getRoomIdByRoomString(currentRoomId) + offset;
        return roomOperation(floor, roomId, offset);
    }

    public Set<LivingRecord> roomOperation(Integer floor, Integer roomId, Integer offset) {
        roomId = findLegalRoom(floor, roomId, offset);
        Optional<FloorArea> optional = floorAreaService.findByFloorAndRoomId(floor, roomId);
        if (optional.isEmpty()) {
            return Set.of();
        }

        var query = livingRecordService.findAllByRoomId(
                getRoomIdString(floor, roomId)
        );
        boolean nextOperation = offset >= 0;
        while (isEmptyRoom(query) && !query.isEmpty()) {
            roomId = findLegalRoom(floor, roomId, offset);
            if (roomId > optional.get().getEndRoomId()) return Set.of();
            query = livingRecordService.findAllByRoomId(
                    getRoomIdString(floor, nextOperation ? roomId++ : roomId--)
            );
        }
        return query;
    }

    boolean isAllEmptyRoomArea(FloorArea floorArea) {
        Integer floor = floorArea.getFloor();
        return isAllEmptyRoomArea(floor, floorArea.getStartRoomId(), floorArea.getEndRoomId(), true);
    }

    boolean isAllEmptyRoomArea(Integer floor, Integer start, Integer end, boolean containStartRoom) {
        if (start == null || end == null) return true;
        if (!containStartRoom) {
            start++;
        }
        for (int i = start; i <= end; i++) {
            boolean isAllNotEmpty = livingRecordService.findAllByRoomId(
                    getRoomIdString(floor, i)
            ).stream().anyMatch(e -> e.getStudent() != null);
            if (isAllNotEmpty) return false;
        }
        return true;
    }

    Integer findLegalRoom(Integer floor, Integer roomId, Integer offset) {
        if (offset == 0) offset = 1;
        while (isIllegalRoom(roomId) && !isLastRoom(floor, roomId)) {
            roomId += offset;
        }
        return roomId;
    }

    Integer getFloorByRoomString(@NotNull String roomIdString) {
        return Character.getNumericValue(
                roomIdString.charAt(1)
        );
    }

    Integer getRoomIdByRoomString(@NotNull String roomId) {
        return Integer.parseInt(roomId.substring(roomId.length() - 2));
    }

    // 沒有這些房間
    boolean isIllegalRoom(Integer roomId) {
        return List.of(10, 12, 14, 16, 36, 38).contains(roomId);
    }

    public boolean isLastRoom(Integer floor, Integer roomId) {
        Optional<FloorArea> optional = floorAreaService.findByFloorAndRoomId(floor, roomId);
        if (optional.isEmpty()) return true;
        FloorArea floorArea = optional.get();
        boolean isLast = roomId >= floorArea.getEndRoomId();
        if (isLast) return true;
        return isAllEmptyRoomArea(floorArea.getFloor(), roomId, floorArea.getEndRoomId(), false);
    }

    public boolean isLastRoom(String roomIdString) {
        Integer floor = getFloorByRoomString(roomIdString);
        Integer roomId = getRoomIdByRoomString(roomIdString);
        return isLastRoom(floor, roomId);
    }

    public boolean isEmptyRoom(@NotNull Set<LivingRecord> livingRecords) {
        return livingRecords.stream().allMatch(e -> e.getStudent() == null);
    }

    public boolean attendance(@NotNull String bedId, AttendanceStatusEnum statusEnum, @NotNull String cadreDiscordId) {
        var op = livingRecordService.findAllByBedId(bedId);
        var studentCadre = discordLinkService.findByDiscordId(cadreDiscordId);
        var cadre = floorAreaCadreService.findByCadreStudent(studentCadre.getStudent());
        if (op.isEmpty() || cadre.isEmpty() || op.get().getStudent() == null) {
            return false;
        }

        attendanceService.save(
                op.get().getStudent(),
                op.get().getBed(),
                statusEnum,
                cadre.get().getCadre()
        );
        return true;
    }

    public void doAttendance(@NotNull String cadreDiscordId, @NotNull List<String> bedIds, boolean isInRoom) {
        for (String bedId : bedIds) {
            if (!bedId.matches(bedIdRegex)) continue;
            attendance(
                    bedId,
                    isInRoom ? AttendanceStatusEnum.IN : AttendanceStatusEnum.OUT,
                    cadreDiscordId
            );
        }
    }

    public Set<LivingRecord> getNotComplete(String discordId) {
        var cadreDiscordLink = discordLinkService.findByDiscordId(discordId);
        if (cadreDiscordLink == null) return null;
        Optional<FloorAreaCadre> floorAreaCadre = floorAreaCadreService.findByCadreStudent(cadreDiscordLink.getStudent());
        return floorAreaCadre.map(this::checkNotComplete).orElse(
                null
        );
    }

    public Set<LivingRecord> checkNotComplete(FloorAreaCadre floorAreaCadre) {
        Integer floor = floorAreaCadre.getFloorArea().getFloor();
        Integer startRoomId = floorAreaCadre.getFloorArea().getStartRoomId();
        Integer endRoomId = floorAreaCadre.getFloorArea().getEndRoomId();
        LocalDate today = LocalDate.now();
        for (int i = startRoomId; i <= endRoomId; i++) {
            String roomId = getRoomIdString(floor, i);
            if (!attendanceService.existByRoomIdAndData(roomId, today)) {
                if (isIllegalRoom(i)) continue;
                var query = livingRecordService.findAllByRoomId(roomId);
                if (query.isEmpty() || isEmptyRoom(query)) continue;
                return query;
            }
        }
        return Set.of();
    }
}
