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
    String infoByDiscordAccOption;
    @Value("${component.menu.repair}")
    String repair;
    @Value("${component.menu.took-coin}")
    String tookCoin;
    @Value("${component.menu.took-coin-search}")
    String tookCoinSearch;
    @Value("${component.menu.option-civil}")
    String civilOption;
    @Value("${component.menu.option-hydro}")
    String hydroOption;
    @Value("${component.menu.option-door}")
    String doorOption;
    @Value("${component.menu.option-air_cond}")
    String airCondOption;
    @Value("${component.menu.option-other}")
    String otherOption;
    @Value("${component.menu.option-wash_and_dry}")
    String washAndDryOption;
    @Value("${component.menu.option-vending}")
    String vendingOption;
    @Value("${component.menu.option-wash_machine}")
    String washingMachineOption;
    @Value("${component.menu.option-dryer}")
    String dryerOption;
    @Value("${component.menu.option-drinking}")
    String drinkingOption;
    @Value("${component.menu.activity}")
    String activity;
    @Value("${component.menu.auth}")
    String auth;
}
