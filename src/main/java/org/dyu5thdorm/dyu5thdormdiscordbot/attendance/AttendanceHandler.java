package org.dyu5thdorm.dyu5thdormdiscordbot.attendance;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.Maintenance;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre.FloorAreaCadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:regex.properties")
public class AttendanceHandler {
    final LivingRecordService livingRecordService;
    final DiscordLinkService discordLinkService;
    final FloorAreaCadreService floorAreaCadreService;
    final FloorAreaService floorAreaService;
    final AttendanceService attendanceService;
    final Maintenance maintenance;
    final NoCallRollDateService noCallRollDateService;

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
    void setup() {
        startLocalTime = LocalTime.of(startTimeHour, startTimeMin);
        endLocalTime = LocalTime.of(endTimeHour, endTimeMin);
    }

    public boolean isDevMode() {
        return maintenance.isMaintenanceStatus();
    }

    public boolean isAttendanceDay(LocalDate localDate) {
        return !noCallRollDateService.exists(localDate);
    }

    public boolean isIllegalTime(@NotNull LocalTime time) {
        return time.isBefore(startLocalTime) || time.isAfter(endLocalTime);
    }

    public boolean isAfter(@NotNull LocalTime time) {
        return time.isAfter(endLocalTime) || time.equals(endLocalTime);
    }

    public Set<LivingRecord> nextRoom(String currentRoomId) {
        return roomOperation(currentRoomId, 1);
    }

    public Set<LivingRecord> prevRoom(String currentRoomId) {
        return roomOperation(currentRoomId, -1);
    }

    public Optional<ErrorType> check(String discordId) {
        Optional<FloorAreaCadre> floorAreaCadre = this.getFloorAreaCadreByDiscordId(discordId);
        if (floorAreaCadre.isEmpty()) {
            return Optional.of(ErrorType.NOT_FLOOR_AREA_CADRE);
        }

        FloorAreaCadre fac = floorAreaCadre.get();
        if (isAllEmptyRoomArea(fac.getFloorArea())) {
            return Optional.of(ErrorType.EMPTY_AREA);
        }

        Set<LivingRecord> notCompleteList = this.getNotComplete(fac);
        if (notCompleteList.isEmpty()) {
            return Optional.of(ErrorType.ATTENDANCE_COMPLETE);
        }

        return Optional.empty();
    }

    public Set<LivingRecord> getRoomStart(String discordId) {
        Optional<ErrorType> errorType = check(discordId);
        if (errorType.isPresent())
            throw new RuntimeException(String.format("錯誤訊息： %s，使用此方法時應先使用check()方法檢查有無錯誤。", errorType.get().getMessage()));
        return this.getNotComplete(discordId);
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

        var query = livingRecordService.findAllByRoomId(getRoomIdString(floor, roomId));
        boolean nextOperation = offset >= 0;
        while (isEmptyRoom(query) && !query.isEmpty()) {
            roomId = findLegalRoom(floor, roomId, offset);
            if (roomId > optional.get().getEndRoomId()) return Set.of();
            query = livingRecordService.findAllByRoomId(getRoomIdString(floor, nextOperation ? roomId++ : roomId--));
        }
        return query;
    }

    boolean isAllEmptyRoomArea(@NotNull FloorArea floorArea) {
        Integer floor = floorArea.getFloor();
        return isAllEmptyRoomArea(floor, floorArea.getStartRoomId(), floorArea.getEndRoomId(), true);
    }

    boolean isAllEmptyRoomArea(Integer floor, Integer start, Integer end, boolean containStartRoom) {
        if (start == null || end == null) return true;
        if (!containStartRoom) {
            start++;
        }
        for (int i = start; i <= end; i++) {
            boolean isAllNotEmpty = livingRecordService.findAllByRoomId(getRoomIdString(floor, i)).stream().anyMatch(e -> e.getStudent() != null);
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
        return Character.getNumericValue(roomIdString.charAt(1));
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
        var cadre = getFloorAreaCadreByDiscordId(cadreDiscordId);
        if (op.isEmpty() || cadre.isEmpty() || op.get().getStudent() == null) {
            return false;
        }

        attendanceService.save(op.get().getStudent(), op.get().getBed(), statusEnum, cadre.get().getCadre());
        return true;
    }

    public void doAttendance(@NotNull String cadreDiscordId, @NotNull List<String> bedIds, boolean isInRoom) {
        for (String bedId : bedIds) {
            if (!bedId.matches(bedIdRegex)) continue;
            attendance(bedId, isInRoom ? AttendanceStatusEnum.IN : AttendanceStatusEnum.OUT, cadreDiscordId);
        }
    }

    public void doAttendance(@NotNull String cadreDiscordId, @NotNull String roomId, Set<Character> bedSet) {
        for (LivingRecord livingRecord : livingRecordService.findAllByRoomId(roomId)) {
            Character queryBed = livingRecord.getBed().getBedId().charAt(5);
            attendance(livingRecord.getBed().getBedId(), bedSet.contains(queryBed) ? AttendanceStatusEnum.OUT : AttendanceStatusEnum.IN, cadreDiscordId);
        }
    }

    public Set<LivingRecord> getNotComplete(String discordId) {
        Optional<FloorAreaCadre> floorAreaCadre = getFloorAreaCadreByDiscordId(discordId);
        return floorAreaCadre.isEmpty() ? Set.of() : getNotComplete(floorAreaCadre.get());
    }

    public Set<LivingRecord> getNotComplete(@NotNull FloorAreaCadre floorAreaCadre) {
        Integer floor = floorAreaCadre.getFloorArea().getFloor();
        Integer startRoomId = floorAreaCadre.getFloorArea().getStartRoomId();
        Integer endRoomId = floorAreaCadre.getFloorArea().getEndRoomId();
        LocalDate today = LocalDate.now();
        for (int i = startRoomId; i <= endRoomId; i++) {
            String roomId = getRoomIdString(floor, i);
            if (!attendanceService.existByRoomIdAndData(roomId, today)) {
                if (isIllegalRoom(i)) continue;
                Set<LivingRecord> query = livingRecordService.findAllByRoomId(roomId);
                if (query.isEmpty() || isEmptyRoom(query)) continue;
                return query;
            }
        }
        return Set.of();
    }

    Optional<FloorAreaCadre> getFloorAreaCadreByDiscordId(String cadreDiscordId) {
        Optional<DiscordLink> cadreDiscordLink = discordLinkService.findByDiscordId(cadreDiscordId);
        if (cadreDiscordLink.isEmpty()) return Optional.empty();
        return floorAreaCadreService.findByCadreStudent(cadreDiscordLink.get().getStudent());
    }

    @Getter
    public enum ErrorType {
        ATTENDANCE_COMPLETE("點名已完成，無法再次啟動點名作業。"),
        NOT_FLOOR_AREA_CADRE("您非特定樓層區域幹部，無法使用此功能。"),
        EMPTY_AREA("您所負責之點名區域皆為空房，因此您無法使用此功能。");

        private final String message;

        ErrorType(String message) {
            this.message = message;
        }
    }
}
