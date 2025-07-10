package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl;

import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public interface RepairHandler {
    RepairModel handle(Repair.Type type, Student reporter, List<String> args) throws IOException;
}
