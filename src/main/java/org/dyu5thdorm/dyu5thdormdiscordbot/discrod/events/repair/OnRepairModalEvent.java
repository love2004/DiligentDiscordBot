package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.RepairmentUtils;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.NormalRepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.RepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.impl.SpecialRepairHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OnRepairModalEvent extends ListenerAdapter {
    final
    ModalIdSet modalIdSet;
    Map<String, RepairHandler> ids;
    Map<Repair.Type, String> channels;
    final
    NormalRepairHandler normalRepairHandler;
    final
    SpecialRepairHandler specialRepairHandler;
    final
    Repair repair;
    final
    DiscordLinkService discordLinkService;
    final ChannelIdSet channelIdSet;
    final RepairmentUtils repairmentUtils;

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
        channels = Map.of(
                Repair.Type.AIR_COND, channelIdSet.getNormalRepairment(),
                Repair.Type.CIVIL, channelIdSet.getNormalRepairment(),
                Repair.Type.DOOR, channelIdSet.getNormalRepairment(),
                Repair.Type.HYDRO, channelIdSet.getNormalRepairment(),
                Repair.Type.OTHER, channelIdSet.getNormalRepairment(),
                // === up normal 校內, below outside 廠商 ===
                Repair.Type.DRINKING, channelIdSet.getWaterDispenserRepairment(), // water dispenser
                Repair.Type.VENDING, channelIdSet.getVendingRepairment(), // vending
                Repair.Type.WASH_AND_DRY, channelIdSet.getMachineRepairment() // wash machine and dryer
        );
    }

    @SneakyThrows
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!ids.containsKey(eventModalId)) return;
        event.deferReply().setEphemeral(true).queue();
        List<String> args = event.getValues().stream().map(
                ModalMapping::getAsString
        ).toList();
        Optional<DiscordLink> discordLink = discordLinkService.findByDiscordId(event.getUser().getId());
        if (discordLink.isEmpty()) {
            event.getHook().sendMessage("無綁定住宿生生份者無法使用此功能。").setEphemeral(true).queue();
            return;
        }

        RepairModel model = ids.get(eventModalId).handle(
                repair.getTypeByModalId(eventModalId),
                discordLink.get().getStudent(),
                args
        );

        if (model == null) {
            event.getHook().sendMessage("報修失敗！請聯絡開發者！").setEphemeral(true).queue();
            return;
        }

        TextChannel repairmentChannel = event.getJDA().getTextChannelById(
                channels.get(
                        model.getType()
                )
        );

        if (repairmentChannel == null) {
            throw new RuntimeException("repairment channel not found");
        }

        repairmentChannel.sendMessageEmbeds(
                repairmentUtils.notificationEmbed(model)
        ).queue();

        event.getHook().sendMessage("""
                # 報修成功！
                - 報修成功後若要更改報修內容(包含取消報修)，請與 宿舍幹部 聯繫更改，__**請勿重複報修**__。
                - 維修人員上班時間為：上課日 星期一 ~ 星期五、星期六不固定、星期日休假。
                - 報修後一定會盡快處理，請您耐心等候(天數約 0 ~ 7 天，若因缺料件而延期會再通知)。
                """).setEphemeral(true).queue();
    }
}
