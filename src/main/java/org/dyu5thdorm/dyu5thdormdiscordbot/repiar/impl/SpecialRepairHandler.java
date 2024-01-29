package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.LineNotify;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.RepairTokenSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SpecialRepairHandler implements RepairHandler {
    final
    LineNotify lineNotify;
    final
    Repair repair;

    Map<Repair.Type, RepairTokenSet.RepairType> lineTokenMap;

    @PostConstruct
    void postConstruct() {
        lineTokenMap = new EnumMap<>(Repair.Type.class);
        lineTokenMap.put(Repair.Type.VENDING, RepairTokenSet.RepairType.VENDING);
        lineTokenMap.put(Repair.Type.WASH_AND_DRY, RepairTokenSet.RepairType.WASH_AND_DRY_MACHINE);
        lineTokenMap.put(Repair.Type.DRINKING, RepairTokenSet.RepairType.WATER_DISPENSER);
    }

    @Override
    public boolean handle(Repair.Type type, Student reporter, List<String> args) {
        String location = args.get(0);
        String description = args.get(1);

        try {
            String message = repair.getLineMessage(reporter, location, description);
            lineNotify.sendMessage(message, this.lineTokenMap.get(type));
            return true;
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }
}
