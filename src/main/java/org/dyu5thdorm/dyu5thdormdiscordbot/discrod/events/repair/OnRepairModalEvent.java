package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.NormalRepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.RepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.SpecialRepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OnRepairModalEvent extends ListenerAdapter {
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
    Map<String, RepairHandler> ids;
    @Autowired
    NormalRepairHandler normalRepairHandler;
    @Autowired
    SpecialRepairHandler specialRepairHandler;
    @Autowired
    Repair repair;
    @Autowired
    LivingRecordService livingRecordService;

    @PostConstruct
    void initIds() {
        ids = Map.of(
                civilModalId, normalRepairHandler,
                hydroModalId, normalRepairHandler,
                doorModalId, normalRepairHandler,
                airCondModalId, normalRepairHandler,
                otherModalId, normalRepairHandler,
                washAndDryModalId, specialRepairHandler,
                vendingModalId, specialRepairHandler,
                drinkingModalId, specialRepairHandler
        );
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();

        if (!ids.containsKey(eventModalId)) return;

        List<String> args = event.getValues().stream().map(
                ModalMapping::getAsString
        ).toList();

        LivingRecord livingRecord = livingRecordService.findLivingRecordByDiscordId(event.getUser().getId());
        if (livingRecord == null) {
            event.reply("非本屆住宿生或無綁定住宿生生份者無法使用此功能。").setEphemeral(true).queue();
            return;
        }

        boolean handle = ids.get(eventModalId).handle(
                repair.getTypeByModalId(eventModalId),
                livingRecord,
                args
        );

        if (!handle) {
            event.reply("報修失敗！請聯絡開發者！").setEphemeral(true).queue();
            return;
        }

        event.reply("報修成功！").setEphemeral(true).queue();
    }
}
