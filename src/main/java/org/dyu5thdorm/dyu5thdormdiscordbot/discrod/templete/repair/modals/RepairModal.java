package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.modals;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RepairModal {
    Modal civilModal;
    Modal hydroModal;
    Modal doorModal;
    Modal airCondModal;
    Modal otherModal;
    Modal washAndDryModal;
    Modal vendingModal;
    Modal drinkingModal;
    @Value("${component.modal.repair-civil}")
    String civilModalId;
    @Value("${component.modal.repair-hydro}")
    String hydroModalId;
    @Value("${component.modal.repair-door}")
    String doorModalId;
    @Value("${component.modal.repair-air_cond}")
    String airCondModalId;
    @Value("${component.modal.repair-other}")
    String otherModalId;
    @Value("${component.modal.repair-wash_and_dry}")
    String washAndDryModalId;
    @Value("${component.modal.repair-vending}")
    String vendingModalId;
    @Value("${component.modal.repair-drinking}")
    String drinkingModalId;
    @Value("${component.modal.repair-first}")
    String firstTextInputId;
    @Value("${component.modal.repair-second}")
    String secondTextInputId;
    @Value("${component.modal.repair-third}")
    String thirdTextInputId;
    @Value("${component.modal.repair-fourth}")
    String fourthTextInputId;

    @PostConstruct
    void init() {
        civilModal = Modal.create(
                civilModalId, "土木工程報修"
        ).addComponents(
                ActionRow.of(TextInput.create(firstTextInputId, "修繕區域", TextInputStyle.SHORT)
                        .setPlaceholder("請填寫維修區域。 例如：5101-A 或 1AB區樓廁所").build()),
                ActionRow.of(TextInput.create(secondTextInputId, "修繕物品", TextInputStyle.SHORT)
                        .setPlaceholder("請填寫維修物品。 例如：檯燈 或 3號衛浴").build()),
                ActionRow.of(TextInput.create(thirdTextInputId, "損壞情形及狀況", TextInputStyle.SHORT)
                        .setPlaceholder("請敘述損壞情形及狀況。 例如：檯燈不亮 或 堵塞").build()),
                ActionRow.of(TextInput.create(fourthTextInputId, "可配合維修時間(公共區域不須填寫)", TextInputStyle.SHORT)
                        .setRequired(false)
                                .setPlaceholder("私人區域若無填寫，則直接進入維修。")
                        .build()
                )
        ).build();

        hydroModal = civilModal.createCopy().setId(hydroModalId).setTitle("水電工程報修").build();
        doorModal = civilModal.createCopy().setId(doorModalId).setTitle("門窗鎖具報修").build();
        airCondModal = civilModal.createCopy().setId(airCondModalId).setTitle("空調設備報修").build();
        otherModal = civilModal.createCopy().setId(otherModalId).setTitle("其他報修").build();
        washAndDryModal = Modal.create(washAndDryModalId, "洗、烘衣機報修")
                .addComponents(
                        ActionRow.of(
                                TextInput.create(firstTextInputId, "具體位置", TextInputStyle.SHORT)
                                        .setPlaceholder("請填寫具體位置。 例如：三樓AB區三號洗衣機。").build()
                        ),
                        ActionRow.of(
                                TextInput.create(secondTextInputId, "損壞情形及狀況", TextInputStyle.SHORT)
                                        .setPlaceholder("請填寫損壞情形及狀況。 例如： 無法運作。").build()
                        )
                )
                .build();
        vendingModal = washAndDryModal.createCopy().setId(vendingModalId).setTitle("販賣機報修").build();
        drinkingModal = washAndDryModal.createCopy().setId(drinkingModalId).setTitle("飲水機報修").build();
    }

    public Modal getModal(Repair.Type type) {
        switch (type) {
            case CIVIL -> {
                return civilModal;
            }
            case HYDRO -> {
                return hydroModal;
            }
            case DOOR -> {
                return doorModal;
            }
            case AIR_COND -> {
                return airCondModal;
            }
            case WASH_AND_DRY -> {
                return washAndDryModal;
            }
            case VENDING -> {
                return vendingModal;
            }
            case DRINKING -> {
                return drinkingModal;
            }
            default -> {
                return otherModal;
            }
        }
    }
}
