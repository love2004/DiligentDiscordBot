package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl;

import jakarta.annotation.PostConstruct;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.modals.RepairModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.LineNotify;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.RepairTokenSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModelFactory;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.crawler.RepairCrawler;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class NormalRepairHandler implements RepairHandler {
    @Autowired
    LineNotify lineNotify;
    @Autowired
    RepairModelFactory factory;
    @Autowired
    Repair repair;
    @Autowired
    RepairCrawler repairCrawler;

    @Override
    public boolean handle(Repair.Type type, LivingRecord reporter, List<String> args) {
        String location = args.get(0);
        String item = args.get(1);
        String description = args.get(2);
        String repairTime = args.get(3);

        try {
            RepairModel model = factory.factory(type, reporter, location, item, description, repairTime);
            String message = repair.getLineMessage(model);
            repairCrawler.repair(model);
            lineNotify.sendMessage(message, RepairTokenSet.RepairType.NORMAL);
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
