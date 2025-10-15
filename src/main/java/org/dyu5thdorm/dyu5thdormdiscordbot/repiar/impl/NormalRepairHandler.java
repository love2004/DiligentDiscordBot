package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl;

import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.crawler.RepairCrawler;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NormalRepairHandler implements RepairHandler {
    final
    Repair repair;
    final
    RepairCrawler repairCrawler;

    @Override
    public RepairModel handle(Repair.Type type, Student reporter, List<String> args) throws IOException {
        String location = args.get(0);
        String item = args.get(1);
        String description = args.get(2);
        String repairTime = args.get(3);

        RepairModel model = RepairModel.builder()
                .type(type)
                .reporter(reporter)
                .location(location)
                .item(item)
                .description(description)
                .repairTime(repairTime)
                .amount(1)
                .unit(RepairModel.Unit.Normal)
                .reportUnit(RepairModel.ReportUnit.Normal)
                .building(RepairModel.Building.Diligent)
                .build();

        repairCrawler.repair(model);
        return model;
    }
}
