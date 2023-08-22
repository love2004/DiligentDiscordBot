package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter(AccessLevel.NONE)
public class RoleIdSet {
    @Value("${role.manager}")
    private String manager;
    @Value("${role.cadre_leader}")
    private String cadreLeader;
    @Value("${role.cadre}")
    private String cadre;
    @Value("${role.dev}")
    private String developer;
    @Value("${role.floor_one}")
    private String floorOne;
    @Value("${role.floor_two}")
    private String floorTwo;
    @Value("${role.floor_three}")
    private String floorThree;
    @Value("${role.floor_four}")
    private String floorFour;
    @Value("${role.floor_five}")
    private String floorFive;
    @Value("${role.floor_six}")
    private String floorSix;
    private Map<Integer, String> floors;

    public String getRoleIdByFloor(int floor) {
        return floors.get(floor);
    }

    @PostConstruct
    void buildFloorsList() {
        floors = new HashMap<>();
        floors.put(1, floorOne);
        floors.put(2, floorTwo);
        floors.put(3, floorThree);
        floors.put(4, floorFour);
        floors.put(5, floorFive);
        floors.put(6, floorSix);
    }
}
