package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.modals;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.RepairTokenSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@PropertySource("classpath:discord.properties")
public class RepairModals {
    Modal normalModal, washAndDryModal, vendingModal, waterModal;
    @Value("${component.modal.repair-normal}")
    String normalModalId;
    @Value("${component.modal.repair-wash-and-dry}")
    String washAndDryModalId;
    @Value("${component.modal.repair-vending}")
    String vendingModalId;
    @Value("${component.modal.repair-water}")
    String waterModalId;
    @Value("${component.modal.repair-items-t}")
    String normalTextInputItemsId;
    @Value("${component.modal.repair-reason-t}")
    String normalTextInputReasonId;
    @Value("${component.modal.repair-time-t}")
    String normalTextInputTimeId;


    @PostConstruct
    void init() {
        buildNormalModal();
        buildWashAndDryModal();
        buildVendingModal();
        buildWaterModal();
    }

    public List<String> getModalIdList() {
        return List.of(
                normalModalId,
                vendingModalId,
                waterModalId,
                washAndDryModalId
        );
    }

    public RepairTokenSet.RepairType getTypeByModalId(String modalId) {
        if (modalId.equalsIgnoreCase(this.getNormalModalId())) return RepairTokenSet.RepairType.NORMAL;
        if (modalId.equalsIgnoreCase(this.getVendingModalId())) return RepairTokenSet.RepairType.VENDING;
        if (modalId.equalsIgnoreCase(this.getWaterModalId())) return RepairTokenSet.RepairType.WATER_DISPENSER;
        if (modalId.equalsIgnoreCase(this.getWashAndDryModalId())) return RepairTokenSet.RepairType.WASH_AND_DRY_MACHINE;
        return null; //TODO: HANDLE
    }

    void buildWaterModal() {
        waterModal = Modal.create(waterModalId, "飲水機報修")
                .addActionRow(
                        TextInput.create(normalTextInputItemsId, "飲水機編號(Water Dispenser Id)", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setMaxLength(50)
                                .setPlaceholder("請填入飲水機編號")
                                .build()
                ).addActionRow(
                        TextInput.create(normalTextInputReasonId, "報修原因", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setPlaceholder("請說明原因。e.g. 按鈕壞掉")
                                .setMaxLength(100)
                                .build()
                ).build();
    }

    void buildVendingModal() {
        vendingModal = Modal.create(vendingModalId, "販賣機報修")
                .addActionRow(
                        TextInput.create(normalTextInputItemsId, "飲水機編號(Vending Dispenser Id)", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setMaxLength(50)
                                .setPlaceholder("請填入販賣機編號")
                                .build()
                ).addActionRow(
                        TextInput.create(normalTextInputReasonId, "報修原因", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setPlaceholder("請說明原因。e.g. 不斷吃錢")
                                .setMaxLength(100)
                                .build()
                ).build();
    }

    void buildWashAndDryModal() {
        washAndDryModal = Modal.create(washAndDryModalId, "洗、烘衣機報修")
                .addActionRow(
                        TextInput.create(normalTextInputItemsId, "洗、烘衣機編號(Machine Id)", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setMaxLength(50)
                                .setPlaceholder("請填入洗、烘衣機編號")
                                .build()
                ).addActionRow(
                        TextInput.create(normalTextInputReasonId, "報修原因", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setPlaceholder("請說明原因。e.g. 吃錢、無法作用")
                                .setMaxLength(100)
                                .build()
                ).build();
    }

    void buildNormalModal() {
        normalModal = Modal.create(normalModalId, "水電土木報修")
                .addActionRow(
                        TextInput.create(normalTextInputItemsId, "報修物品(公共區域請註明範圍)", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setMaxLength(50)
                                .setPlaceholder("公共區域請註明範圍。e.g. 1樓3號浴室")
                                .build()
                ).addActionRow(
                        TextInput.create(normalTextInputReasonId, "報修原因", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .setPlaceholder("請說明原因。e.g. 蓮蓬頭掉落")
                                .setMaxLength(100)
                                .build()
                ).addActionRow(
                        TextInput.create(normalTextInputTimeId, "可配合時間(若為公共區域則不用填寫)", TextInputStyle.PARAGRAPH)
                                .setRequired(false)
                                .setMaxLength(50)
                                .setPlaceholder("填寫可配合時間，若為公共區域則不用填。 e.g. 禮拜三下午")
                                .build()
                )
                .build();
    }
}
