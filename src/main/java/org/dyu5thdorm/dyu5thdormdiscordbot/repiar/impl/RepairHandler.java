package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl;

import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RepairHandler {
    boolean handle(Repair.Type type, LivingRecord reporter, List<String> args);
}
