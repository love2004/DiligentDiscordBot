package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter(AccessLevel.NONE)
public class ModalIdSet {
    @Value("${component.modal.auth}")
    String auth;
    @Value("${component.modal.student-info-by-bedId}")
    String searchByBI; // BI = Bed Id
    @Value("${component.modal.student-info-by-studentId}")
    String searchBySI;
    @Value("${component.modal.student-info-by-name}")
    String searchByN; // N = name
    @Value("${component.modal.repair-civil}")
    String repairCivil;
    @Value("${component.modal.repair-hydro}")
    String repairHydro;
    @Value("${component.modal.repair-door}")
    String repairDoor;
    @Value("${component.modal.repair-air_cond}")
    String repairAirCond;
    @Value("${component.modal.repair-other}")
    String repairOther;
    @Value("${component.modal.repair-wash_and_dry}")
    String repairWashAndDry;
    @Value("${component.modal.repair-vending}")
    String repairVending;
    @Value("${component.modal.repair-drinking}")
    String repairDrinking;
    @Value("${component.modal.took-coin-vending}")
    String tookCoinVending;
    @Value("${component.modal.took-coin-wash-machine}")
    String tookCoinWashMachine;
    @Value("${component.modal.took-coin-dryer}")
    String tookCoinDryer;
    @Value("${component.modal.took-coin-return}")
    String tookCoinReturn;
    @Value("${component.modal.request-for-leave}")
    String reqForLeave;
    @Value("${component.modal.text-input-first}")
    String firstTextInput;
    @Value("${component.modal.text-input-second}")
    String secondTextInput;
    @Value("${component.modal.text-input-third}")
    String thirdTextInput;
    @Value("${component.modal.text-input-fourth}")
    String fourthTextInput;
}
