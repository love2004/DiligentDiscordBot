package org.dyu5thdorm.dyu5thdormdiscordbot.repiar;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.stereotype.Component;

@Component
@Data
public class RepairModelFactory {
    public RepairModel factory(Repair.Type type,
                               Student reporter,
                               String location,
                               String item,
                               String description,
                               String repairTime) {
        return new RepairModel(
                type,
                reporter,
                RepairModel.Building.Diligent,
                location,
                item,
                RepairModel.Unit.Normal,
                1,
                description,
                repairTime,
                RepairModel.ReportUnit.Normal
        );
    }
}
