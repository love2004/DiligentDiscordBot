package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
