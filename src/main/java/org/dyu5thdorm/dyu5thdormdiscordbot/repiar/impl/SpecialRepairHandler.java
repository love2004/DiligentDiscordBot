package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl;

import org.dyu5thdorm.dyu5thdormdiscordbot.line.LineNotify;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.RepairTokenSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SpecialRepairHandler implements RepairHandler {
    final
    LineNotify lineNotify;
    final
    Repair repair;

    public SpecialRepairHandler(Repair repair, LineNotify lineNotify) {
        this.repair = repair;
        this.lineNotify = lineNotify;
    }

    @Override
    public boolean handle(Repair.Type type, Student reporter, List<String> args) {
        String location = args.get(0);
        String description = args.get(1);

        try {
            String message = repair.getLineMessage(reporter, location, description);
            lineNotify.sendMessage(message, adapter(type));
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    RepairTokenSet.RepairType adapter(Repair.Type type) {
        switch (type) {
            case WASH_AND_DRY -> {
                return RepairTokenSet.RepairType.WASH_AND_DRY_MACHINE;
            }
            case VENDING -> {
                return RepairTokenSet.RepairType.VENDING;
            }
            case DRINKING -> {
                return RepairTokenSet.RepairType.WATER_DISPENSER;
            }
            default -> {
                return null;
            }
        }
    }
}
