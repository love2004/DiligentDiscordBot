package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.NormalRepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.RepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.SpecialRepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OnRepairModalEvent extends ListenerAdapter {
    final
    ModalIdSet modalIdSet;
    Map<String, RepairHandler> ids;
    final
    NormalRepairHandler normalRepairHandler;
    final
    SpecialRepairHandler specialRepairHandler;
    final
    Repair repair;
    final
    DiscordLinkService discordLinkService;

    public OnRepairModalEvent(ModalIdSet modalIdSet, NormalRepairHandler normalRepairHandler, SpecialRepairHandler specialRepairHandler, Repair repair, DiscordLinkService discordLinkService) {
        this.modalIdSet = modalIdSet;
        this.normalRepairHandler = normalRepairHandler;
        this.specialRepairHandler = specialRepairHandler;
        this.repair = repair;
        this.discordLinkService = discordLinkService;
    }

    @PostConstruct
    void initIds() {
        ids = Map.of(
                modalIdSet.getRepairCivil(), normalRepairHandler,
                modalIdSet.getRepairHydro(), normalRepairHandler,
                modalIdSet.getRepairDoor(), normalRepairHandler,
                modalIdSet.getRepairAirCond(), normalRepairHandler,
                modalIdSet.getRepairOther(), normalRepairHandler,
                modalIdSet.getRepairWashAndDry(), specialRepairHandler,
                modalIdSet.getRepairVending(), specialRepairHandler,
                modalIdSet.getRepairDrinking(), specialRepairHandler
        );
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!ids.containsKey(eventModalId)) return;
        event.deferReply().setEphemeral(true).queue();
        List<String> args = event.getValues().stream().map(
                ModalMapping::getAsString
        ).toList();
        DiscordLink discordLink = discordLinkService.findByDiscordId(event.getUser().getId());
        if (discordLink == null) {
            event.getHook().sendMessage("無綁定住宿生生份者無法使用此功能。").setEphemeral(true).queue();
            return;
        }

        boolean handle = ids.get(eventModalId).handle(
                repair.getTypeByModalId(eventModalId),
                discordLink.getStudent(),
                args
        );

        if (!handle) {
            event.getHook().sendMessage("報修失敗！請聯絡開發者！").setEphemeral(true).queue();
            return;
        }

        event.getHook().sendMessage("""
                # 報修成功！
                - 報修成功後若要更改報修內容(包含取消報修)，請與 宿舍幹部 聯繫更改，__**請勿重複報修**__。
                - 維修人員上班時間為：上課日 星期一 ~ 星期五、星期六不固定、星期日休假。
                - 報修後一定會盡快處理，請您耐心等候(天數約 0 ~ 7 天，若因缺料件而延期會再通知)。
                """).setEphemeral(true).queue();
    }
}
