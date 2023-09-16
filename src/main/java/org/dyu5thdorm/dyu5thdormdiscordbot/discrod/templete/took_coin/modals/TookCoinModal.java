package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.modals;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TookCoinModal {
    Modal washMachineModal, dryerModal, vendingModal;
    @Autowired
    ModalIdSet modalIdSet;

    @PostConstruct
    void init() {
        TextInput description = TextInput.create(modalIdSet.getSecondTextInput(), "故障情況說明", TextInputStyle.PARAGRAPH)
                .setPlaceholder("請說明故障情形。例如：卡幣")
                .setRequiredRange(0, 30)
                .build();
        TextInput coin = TextInput.create(modalIdSet.getThirdTextInput(), "卡幣金額", TextInputStyle.SHORT)
                .setPlaceholder("請輸入卡幣金額。例如：87")
                .setRequiredRange(1, 2)
                .build();
        TextInput time = TextInput.create(modalIdSet.getFourthTextInput(), "發生時間 格式：[月+日 時+分]", TextInputStyle.PARAGRAPH)
                .setPlaceholder("請輸入發生時間。\n格式(24小時制)：[月+日 時+分]\n例如：0807 2030")
                .setRequiredRange(9, 9)
                .build();

        washMachineModal = Modal.create(modalIdSet.getTookCoinWashMachine(), "洗衣機吃錢登記")
                .addActionRow(
                        TextInput.create(modalIdSet.getFirstTextInput(), "發生地點(樓層區域)", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("請填寫樓層區域。\n格式：[樓層+區域]\n例如：1AB")
                                .setRequiredRange(3, 3)
                                .build()

                )
                .addActionRow(description)
                .addActionRow(coin)
                .addActionRow(time)
                .build();
        dryerModal = washMachineModal.createCopy().setTitle("烘衣機吃錢登記").setId(modalIdSet.getTookCoinDryer()).build();
        vendingModal = Modal.create(modalIdSet.getTookCoinVending(), "販賣機吃錢登記")
                .addActionRow(
                        TextInput.create(modalIdSet.getFirstTextInput(), "發生地點(樓層)", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("請填寫樓層。\n格式：[樓層]\n例如：1")
                                .setRequiredRange(1, 1)
                                .build()

                )
                .addActionRow(description)
                .addActionRow(coin)
                .addActionRow(time)
                .build();
    }

    public Modal getModalByType(TookCoin.Type type) {
        switch (type) {
            case WASH_MACHINE -> {
                return this.washMachineModal;
            }
            case DRYER -> {
                return this.dryerModal;
            }
            case VENDING -> {
                return this.vendingModal;
            }
        }
        return null;
    }
}
