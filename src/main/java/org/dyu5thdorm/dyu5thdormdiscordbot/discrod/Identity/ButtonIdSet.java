package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter(AccessLevel.NONE)
public class ButtonIdSet {
    @Value("${component.button.auth}")
    String auth;
    @Value("${component.button.repair}")
    String repair;
    @Value("${component.button.student-info-by-discord}")
    String searchByDiscordId;
    @Value("${component.button.student-info-by-bedId}")
    String searchByBedId;
    @Value("${component.button.student-info-by-studentId}")
    String searchByStudentId;
    @Value("${component.button.student-info-by-name}")
    String searchByStudentName;
    @Value("${component.button.shutdown}")
    String shutdown;
    @Value("${component.button.maintenance}")
    String maintenance;
    @Value("${component.button.generate-rules}")
    String generateRules;
    @Value("${component.button.generate-request-leave}")
    String generateReqLev;
    @Value("${component.button.generate-request-auth}")
    String generateReqAuth;
    @Value("${component.button.generate-request-repair}")
    String generateReqRepair;
    @Value("${component.button.generate-request-cadre}")
    String generateReqCadre;
    @Value("${component.button.took-coin}")
    String tookCoin;
}
