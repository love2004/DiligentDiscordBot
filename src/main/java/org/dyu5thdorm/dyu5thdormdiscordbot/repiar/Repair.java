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
        CIVIL("001", "土木工程"),
        HYDRO("002", "水電工程"),
        DOOR("014", "門窗鎖具"),
        AIR_COND("005", "冷氣設備"),
        OTHER("006", "其他"),
        WASH_AND_DRY("N.A", "洗烘衣機"),
        VENDING("N.A", "販賣機"),
        DRINKING("N.A", "飲水機");

        private final String id;
        private final String name;
        Type(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public Type getTypeByModalId(String id) {
        return modalMapping.getOrDefault(id, null);
    }

    public Type getTypeByMenuId(String id) {
        return menuMapping.getOrDefault(id, null);
    }

    @Deprecated
    public String getLineMessage(@NotNull Student reporter, String location, String description) {
        return "\n學號：" + reporter.getStudentId() +
                "\n姓名：" + reporter.getName() +
                "\n電話：" + reporter.getPhoneNumber() +
                "\n報修區域：" + location +
                "\n報修原因：" + description;
    }
}
