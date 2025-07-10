package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl;

import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialRepairHandler implements RepairHandler {
    final
    Repair repair;

    @Override
    public RepairModel handle(Repair.Type type, Student reporter, List<String> args) {
        String location = args.get(0);
        String description = args.get(1);

        return RepairModel.builder()
                .reporter(reporter)
                .location(location)
                .description(description)
                .item("")
                .unit(RepairModel.Unit.None)
                .repairTime("")
                .amount(1)
                .building(RepairModel.Building.Diligent)
                .type(type)
                .reportUnit(RepairModel.ReportUnit.Normal)
                .build();
    }
}
