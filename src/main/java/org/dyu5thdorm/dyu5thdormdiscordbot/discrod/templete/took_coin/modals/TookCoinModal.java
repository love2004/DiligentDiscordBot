package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.modals;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoinHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class TookCoinModal {
    final
    ModalIdSet modalIdSet;

    Modal washMachineModal, dryerModal, vendingModal;
    DateTimeFormatter formatter;
    TextInput.Builder time;
    @Value("${datetime-textinput.format}")
    String format;


    @PostConstruct
    void init() {
        formatter = DateTimeFormatter.ofPattern(format);
        TextInput.Builder description = TextInput.create(modalIdSet.getSecondTextInput(), "故障情況說明", TextInputStyle.PARAGRAPH)
                .setRequiredRange(0, 50);
        TextInput coin = TextInput.create(modalIdSet.getThirdTextInput(), "卡幣金額", TextInputStyle.SHORT)
                .setPlaceholder("請輸入卡幣金額。例如：87")
                .setRequiredRange(1, 2)
                .build();
        time = TextInput.create(modalIdSet.getFourthTextInput(), "發生時間 格式：[月+日 時+分] \n(自動帶入三分鐘前的時間)", TextInputStyle.PARAGRAPH)
                .setPlaceholder("請輸入發生時間。\n格式(24小時制)：[月+日 時+分]\n例如：0807 2030")
                .setRequiredRange(9, 9);

        washMachineModal = Modal.create(modalIdSet.getTookCoinWashMachine(), "洗衣機吃錢登記")
                .addActionRow(
                        TextInput.create(modalIdSet.getFirstTextInput(), "發生地點(樓層區域)", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("請填寫樓層區域。\n格式：[樓層+區域]\n例如：1AB")
                                .setRequiredRange(3, 3)
                                .build()

                )
                .addActionRow(
                        description.setPlaceholder(
                        """
                        請說明是哪台機器。
                        超過1台時，面對它加註中左右。
                        例如：中間洗衣機兩次共投入40元但機器都沒轉。
                        """).build()
                )
                .addActionRow(coin)
                .addActionRow(time.build())
                .build();
        dryerModal = washMachineModal.createCopy().setTitle("烘衣機吃錢登記").setId(modalIdSet.getTookCoinDryer()).build();
        vendingModal = Modal.create(modalIdSet.getTookCoinVending(), "販賣機吃錢登記")
                .addActionRow(
                        TextInput.create(modalIdSet.getFirstTextInput(), "發生地點(樓層)", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("請填寫樓層。\n格式：[樓層]\n例如：1")
                                .setRequiredRange(1, 1)
                                .build()

                )
                .addActionRow(description.setPlaceholder(
                        """
                        盡量清楚說明，以利廠商盡快修復販賣機。
                        例如：投50元選10元的麥香綠茶沒找40元、投25元選B3五香海苔東西沒掉出來
                        """
                ).build())
                .addActionRow(coin)
                .addActionRow(time.build())
                .build();
    }

    public Modal getModalByType(TookCoinHandler.MachineType type) {
        Modal returnModal;
        switch (type) {
            case WASH_MACHINE -> returnModal = this.washMachineModal;
            case DRYER -> returnModal = this.dryerModal;
            case VENDING -> returnModal = this.vendingModal;
            default -> returnModal = null;
        }
        return timeAutoFillOut(returnModal);
    }

    Modal timeAutoFillOut(Modal modal) {
        LocalDateTime nowMinusFiveMin = LocalDateTime.now().minusMinutes(3);
        Modal.Builder modalCopy = modal.createCopy();
        TextInput.Builder timeBuilderCopy = time;
        modalCopy.getComponents().get(3).updateComponent(
                modalIdSet.getFourthTextInput(),
                timeBuilderCopy
                        .setValue(
                                formatter.format(nowMinusFiveMin)
                        )
                        .build()
        );
        return modalCopy.build();
    }
}
