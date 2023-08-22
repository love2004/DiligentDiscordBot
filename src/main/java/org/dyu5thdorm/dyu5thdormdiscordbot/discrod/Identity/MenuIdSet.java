package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter(AccessLevel.NONE)
public class MenuIdSet {
    @Value("${component.menu.student-info-by-discord}")
    String infoByDiscordAcc;
    @Value("${component.menu.repair}")
    String repair;
    @Value("${component.menu.repair-civil}")
    String civil;
    @Value("${component.menu.repair-hydro}")
    String hydro;
    @Value("${component.menu.repair-door}")
    String door;
    @Value("${component.menu.repair-air_cond}")
    String airCond;
    @Value("${component.menu.repair-other}")
    String other;
    @Value("${component.menu.repair-wash_and_dry}")
    String washAndDry;
    @Value("${component.menu.repair-vending}")
    String vending;
    @Value("${component.menu.repair-drinking}")
    String drinking;
}
