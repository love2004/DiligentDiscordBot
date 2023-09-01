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
    @Value("${channel.leave}")
    String leave;
    @Value("${channel.cadre-button}")
    String cadreButton;
    @Value("${channel.repair}")
    String repair;
    @Value("${channel.admin.operation}")
    String adminOperation;
    @Value("${channel.rules}")
    String rules;
    @Value("${channel.request-leave}")
    String reqLev;
    @Value("${channel.took-coin-cadre}")
    String tookCoinCadre;
}
