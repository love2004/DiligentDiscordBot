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
    @Value("${component.modal.auth-student-id-t}")
    String authSITextInput; // SI = Student Id
    @Value("${component.modal.auth-phone-number-t}")
    String authPNTextInput; // PN = Phone Number
    @Value("${component.modal.student-info-by-bedId}")
    String searchByBI; // BI = Bed Id
    @Value("${component.modal.student-info-by-bedId-t}")
    String searchByBITextInput;
    @Value("${component.modal.student-info-by-studentId}")
    String searchBySI;
    @Value("${component.modal.student-info-by-studentId-t}")
    String searchBySITextInput;
    @Value("${component.modal.student-info-by-name}")
    String searchByN; // N = name
    @Value("${component.modal.student-info-by-name-t}")
    String searchByNTextInput; // N = name
    @Value("${component.modal.repair-civil}")
    String civil;
    @Value("${component.modal.repair-hydro}")
    String hydro;
    @Value("${component.modal.repair-door}")
    String door;
    @Value("${component.modal.repair-air_cond}")
    String airCond;
    @Value("${component.modal.repair-other}")
    String other;
    @Value("${component.modal.repair-wash_and_dry}")
    String washAndDry;
    @Value("${component.modal.repair-vending}")
    String vending;
    @Value("${component.modal.repair-drinking}")
    String drinking;
    @Value("${component.modal.repair-first-t}")
    String repairFirstTextInput;
    @Value("${component.modal.repair-second-t}")
    String repairSecondTextInput;
    @Value("${component.modal.repair-third-t}")
    String repairThirdTextInput;
    @Value("${component.modal.repair-fourth-t}")
    String repairFourthTextInput;
}
