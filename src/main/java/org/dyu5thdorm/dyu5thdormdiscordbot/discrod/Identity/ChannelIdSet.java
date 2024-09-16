package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter(AccessLevel.NONE)
public class ChannelIdSet {
    @Value("${channel.auth}")
    String auth;
    @Value("${channel.auth-logger}")
    String authLogger;
    @Value("${channel.announcement}")
    String announcement;
    @Value("${channel.leave}")
    String leave;
    @Value("${channel.cadre-button}")
    String cadreButton;
    @Value("${channel.repair}")
    String repair;
    @Value("${channel.took-coin}")
    String tookCoin;
    @Value("${channel.admin.operation}")
    String adminOperation;
    @Value("${channel.rules}")
    String rules;
    @Value("${channel.request-leave}")
    String reqLev;
    @Value("${channel.request-leave-cadre}")
    String reqLevCadre;
    @Value("${channel.took-coin-cadre}")
    String tookCoinCadre;
    @Value("${channel.took-coin-get-back-cadre}")
    String tookCoinGetBackCadre;
    @Value("${channel.floor-one}")
    String floorOne;
    @Value("${channel.floor-two}")
    String floorTwo;
    @Value("${channel.floor-three}")
    String floorThree;
    @Value("${channel.floor-four}")
    String floorFour;
    @Value("${channel.floor-five}")
    String floorFive;
    @Value("${channel.floor-six}")
    String floorSix;
    @Value("${channel.public}")
    String publicChannel;
    @Value("${channel.lottery}")
    String lottery;
    @Value("${channel.vote}")
    String vote;
    @Value("${channel.leader}")
    String leader;
    @Value("${channel.return-by-firm}")
    String returnByFirm;
}
