package org.dyu5thdorm.dyu5thdormdiscordbot.took_coin;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.MachineDTO;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.TookCoinService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TookCoinHandler {
    final TookCoinService tookCoinService;
    final DiscordLinkService discordLinkService;

    @Value("${datetime.format}")
    String dateTimeFormat;
    @Value("${date.format}")
    String dateFormat;
    @Value("${path-washing-and-dryer}")
    String washingAndDryerDirPath;
    @Value("${path-vending}")
    String vendingDirPath;
    Map<MachineType, String> machineTypeStringMap;

    @PostConstruct
    void setup() throws IOException {
        String currentDir = System.getProperty("user.dir");
        machineTypeStringMap = new EnumMap<>(MachineType.class);
        machineTypeStringMap.put(MachineType.WASH_MACHINE, "洗衣機");
        machineTypeStringMap.put(MachineType.DRYER, "烘衣機");
        machineTypeStringMap.put(MachineType.VENDING, "販賣機");
        vendingDirPath = currentDir + vendingDirPath;
        washingAndDryerDirPath = currentDir + washingAndDryerDirPath;
    }

    FloorArea getFloorArea(MachineType type, String floorOrFloorArea) {
        FloorArea floorArea = new FloorArea();
        // VENDING only in CD area
        if (type == MachineType.VENDING) {
            floorArea.setFloor(Integer.parseInt(floorOrFloorArea));
            if (floorArea.getFloor() == 1L) {
                floorArea.setAreaId("AB");
            } else {
                floorArea.setAreaId("CD");
            }
        } else {
            Integer floor = Integer.parseInt(floorOrFloorArea.substring(0, 1));
            String areaId = floorOrFloorArea.substring(1, 3);
            floorArea.setFloor(floor);
            floorArea.setAreaId(areaId);
        }

        return floorArea;
    }

    public FailReason record(MachineType type, List<String> args, Student student) {
        org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin tookCoinModel = new org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin();
        tookCoinModel.setStudent(student);
        FloorArea floorArea = this.getFloorArea(type, args.get(0));

        tookCoinModel.setFloor(floorArea);
        tookCoinModel.setDescription(args.get(1));
        tookCoinModel.setCoinAmount(
                Integer.valueOf(args.get(2))
        );
        tookCoinModel.setMachine(type.name());
        tookCoinModel.setEventTime(
                getLocalDateTime(args.get(3))
        );
        if (tookCoinModel.getEventTime().isAfter(LocalDateTime.now())) {
            return FailReason.DATE_AFTER_NOW;
        }

        if (tookCoinService.existsByTimeAndStudentId(
                tookCoinModel.getEventTime(), student.getStudentId())
        ) {
            return FailReason.TIME_REPEAT;
        }

        tookCoinService.save(tookCoinModel);

        return FailReason.NONE;
    }

    public LocalDateTime getLocalDateTime(@NotNull String date) {
        int year = LocalDate.now().getYear();
        int month = Integer.parseInt(date.substring(0, 2));
        int day = Integer.parseInt(date.substring(2, 4));
        int hour = Integer.parseInt(date.substring(5, 7));
        int minute = Integer.parseInt(date.substring(7, 9));
        return LocalDateTime.of(
                year, month, day, hour, minute
        );
    }

    public Set<org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin> getRecordUnGetByDiscordId(String discordId) {
        Optional<DiscordLink> discordLink = getDiscordLinkByDiscordId(discordId);
        if (discordLink.isEmpty()) return Set.of();
        Student student = discordLink.get().getStudent();

        return tookCoinService.findUnGetByStudentId(student.getStudentId());
    }

    Optional<DiscordLink> getDiscordLinkByDiscordId(String discordId) {
        return discordLinkService.findByDiscordId(discordId);
    }

    public void dumpFile() throws IOException {
        this.createDirectory(MachineType.VENDING);
        this.createDirectory(MachineType.WASH_MACHINE);
        writeToFile(MachineType.VENDING);
        writeToFile(MachineType.WASH_MACHINE);
    }

    void createDirectory(MachineType type) {
        File file;
        if (type == MachineType.VENDING) {
            file = new File(vendingDirPath);
        } else if (type == MachineType.DRYER || type == MachineType.WASH_MACHINE) {
            file = new File(washingAndDryerDirPath);
        } else {
            throw new RuntimeException("error machine type");
        }

        if (!file.exists()) file.mkdirs();
    }

    void writeToFile(MachineType type) throws IOException {
        String header = """
                樓層, 區域, 床號, 學號, 姓名, 機器, 敘述, 金額, 發生時間, 回報時間
                """;
        StringBuilder builder = new StringBuilder();
        Path path;
        LocalDateTime now = LocalDateTime.now();
        String fileName = DateTimeFormatter.ofPattern(dateFormat).format(now) + ".csv";
        if (type == MachineType.WASH_MACHINE || type == MachineType.DRYER) {
            path = Path.of(washingAndDryerDirPath + "wd-" + fileName);
            builder.append(
                    getCsvFileContent(
                            MachineType.WASH_MACHINE
                    )
            );
            builder.append(
                    getCsvFileContent(
                            MachineType.DRYER
                    )
            );
        } else {
            path = Path.of(vendingDirPath + "v-" + fileName);
            builder.append(
                    getCsvFileContent(
                            MachineType.VENDING
                    )
            );
        }

        if (builder.isEmpty()) {
            return;
        } else {
            builder.insert(0, header);
        }

        Files.writeString(
                path,
                builder.toString(),
                Charset.forName("big5")
        );
    }

    String getCsvFileContent(MachineType type) {
        List<MachineDTO> dto = tookCoinService.findNotGetMachine(type);
        StringBuilder sb = new StringBuilder();
        for (MachineDTO machineDTO : dto) {
            sb.append(machineDTO.getFloor());
            sb.append(", ");
            sb.append(machineDTO.getArea());
            sb.append(", ");
            sb.append(machineDTO.getBedId());
            sb.append(", ");
            sb.append(machineDTO.getStudentId());
            sb.append(", ");
            sb.append(machineDTO.getName());
            sb.append(", ");
            sb.append(
                    machineTypeStringMap.get(type)
            );
            sb.append(", ");
            sb.append(machineDTO.getDescription());
            sb.append(", ");
            sb.append(machineDTO.getCoinAmount());
            sb.append(", ");
            sb.append(
                    DateTimeFormatter.ofPattern(dateTimeFormat).format(
                            machineDTO.getEventTime()
                    )
            );
            sb.append(", ");
            sb.append(
                    DateTimeFormatter.ofPattern(dateTimeFormat).format(
                            machineDTO.getRecordTime()
                    )
            );
            sb.append("\n");
        }

        return sb.toString();
    }

    public enum MachineType {WASH_MACHINE, DRYER, VENDING}

    public enum FailReason {DATE_AFTER_NOW, TIME_REPEAT, NONE}

}
