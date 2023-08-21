package org.dyu5thdorm.dyu5thdormdiscordbot.repiar;

import jakarta.annotation.PostConstruct;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.modals.RepairModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Repair {
    @Value("${component.menu.repair}")
    String menuId;
    @Value("${component.menu.repair-civil}")
    String civilId; // 土木
    @Value("${component.menu.repair-hydro}")
    String hydroId; // 水電
    @Value("${component.menu.repair-door}")
    String doorId; // 門窗鎖具
    @Value("${component.menu.repair-air_cond}")
    String airCondId; // 空調
    @Value("${component.menu.repair-other}")
    String otherId; // 其他
    @Value("${component.menu.repair-wash_and_dry}")
    String washAndDryId; // 洗烘衣機
    @Value("${component.menu.repair-vending}")
    String vendingId; // 販賣機
    @Value("${component.menu.repair-drinking}")
    String drinkingId; // 飲水機

    @Value("${component.modal.repair-civil}")
    String civilModalId;
    @Value("${component.modal.repair-hydro}")
    String hydroModalId;
    @Value("${component.modal.repair-door}")
    String doorModalId;
    @Value("${component.modal.repair-air_cond}")
    String airCondModalId;
    @Value("${component.modal.repair-other}")
    String otherModalId;
    @Value("${component.modal.repair-wash_and_dry}")
    String washAndDryModalId;
    @Value("${component.modal.repair-vending}")
    String vendingModalId;
    @Value("${component.modal.repair-drinking}")
    String drinkingModalId;

    Map<String, Type> modalMapping, menuMapping;

    @PostConstruct
    void init() {
        modalMapping = Map.of(
                civilModalId, Type.CIVIL,
                hydroModalId, Type.HYDRO,
                doorModalId, Type.DOOR,
                airCondModalId, Type.AIR_COND,
                otherModalId, Type.OTHER,
                washAndDryModalId, Type.WASH_AND_DRY,
                vendingModalId, Type.VENDING,
                drinkingModalId, Type.DRINKING
        );

        menuMapping = Map.of(
                civilId, Type.CIVIL,
                hydroId, Type.HYDRO,
                doorId, Type.DOOR,
                airCondId, Type.AIR_COND,
                otherId, Type.OTHER,
                washAndDryId, Type.WASH_AND_DRY,
                vendingId, Type.VENDING,
                drinkingId, Type.DRINKING
        );
    }

    public enum Type {
        CIVIL, HYDRO, DOOR, AIR_COND, OTHER, WASH_AND_DRY, VENDING, DRINKING
    }

    public Type getTypeByModalId(String id) {
        return modalMapping.getOrDefault(id, null);
    }

    public Type getTypeByMenuId(String id) {
        return menuMapping.getOrDefault(id, null);
    }

    public String getLineMessage(RepairModel repairModel) {
        StringBuilder builder = new StringBuilder();
        Student reporter = repairModel.getReporter().getStudent();
        builder.append("\n學號：").append(reporter.getStudentId());
        builder.append("\n姓名：").append(reporter.getName());
        builder.append("\n電話：").append(reporter.getPhoneNumber());
        builder.append("\n報修區域：").append(repairModel.location);
        builder.append("\n報修物品：").append(repairModel.item);
        builder.append("\n報修原因：").append(repairModel.description);
        builder.append("\n可配合時間：").append(repairModel.repairTime);

        return builder.toString();
    }

    public String getLineMessage(LivingRecord record, String location, String description) {
        StringBuilder builder = new StringBuilder();
        Student reporter = record.getStudent();
        builder.append("\n學號：").append(reporter.getStudentId());
        builder.append("\n姓名：").append(reporter.getName());
        builder.append("\n電話：").append(reporter.getPhoneNumber());
        builder.append("\n報修區域：").append(location);
        builder.append("\n報修原因：").append(description);
        return builder.toString();
    }

    public boolean isNormalType(Type type) {
        return List.of(
                Type.CIVIL, Type.HYDRO, Type.OTHER, Type.DOOR, Type.AIR_COND
        ).contains(type);
    }

}
