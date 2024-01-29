package org.dyu5thdorm.dyu5thdormdiscordbot.repiar;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Repair {
    final
    MenuIdSet menuIdSet;
    final
    ModalIdSet modalIdSet;

    Map<String, Type> modalMapping, menuMapping;

    @PostConstruct
    void init() {
        modalMapping = Map.of(
                modalIdSet.getRepairCivil(), Type.CIVIL,
                modalIdSet.getRepairHydro(), Type.HYDRO,
                modalIdSet.getRepairDoor(), Type.DOOR,
                modalIdSet.getRepairAirCond(), Type.AIR_COND,
                modalIdSet.getRepairOther(), Type.OTHER,
                modalIdSet.getRepairWashAndDry(), Type.WASH_AND_DRY,
                modalIdSet.getRepairVending(), Type.VENDING,
                modalIdSet.getRepairDrinking(), Type.DRINKING
        );

        menuMapping = Map.of(
                menuIdSet.getCivilOption(), Type.CIVIL,
                menuIdSet.getHydroOption(), Type.HYDRO,
                menuIdSet.getDoorOption(), Type.DOOR,
                menuIdSet.getAirCondOption(), Type.AIR_COND,
                menuIdSet.getOtherOption(), Type.OTHER,
                menuIdSet.getWashAndDryOption(), Type.WASH_AND_DRY,
                menuIdSet.getVendingOption(), Type.VENDING,
                menuIdSet.getDrinkingOption(), Type.DRINKING
        );
    }

    @Getter
    public enum Type {
        CIVIL("001"),
        HYDRO("002"),
        DOOR("014"),
        AIR_COND("005"),
        OTHER("006"),
        WASH_AND_DRY("N.A"),
        VENDING("N.A"),
        DRINKING("N.A");

        private final String id;
        Type(String id) {
            this.id = id;
        }
    }

    public Type getTypeByModalId(String id) {
        return modalMapping.getOrDefault(id, null);
    }

    public Type getTypeByMenuId(String id) {
        return menuMapping.getOrDefault(id, null);
    }

    public String getLineMessage(@NotNull RepairModel repairModel) {
        StringBuilder builder = new StringBuilder();
        Student reporter = repairModel.getReporter();
        builder.append("\n學號：").append(reporter.getStudentId());
        builder.append("\n姓名：").append(reporter.getName());
        builder.append("\n電話：").append(reporter.getPhoneNumber());
        builder.append("\n報修區域：").append(repairModel.location);
        builder.append("\n報修物品：").append(repairModel.item);
        builder.append("\n報修原因：").append(repairModel.description);
        builder.append("\n可配合時間：").append(repairModel.repairTime);
        return builder.toString();
    }

    public String getLineMessage(@NotNull Student reporter, String location, String description) {
        return "\n學號：" + reporter.getStudentId() +
                "\n姓名：" + reporter.getName() +
                "\n電話：" + reporter.getPhoneNumber() +
                "\n報修區域：" + location +
                "\n報修原因：" + description;
    }
}
