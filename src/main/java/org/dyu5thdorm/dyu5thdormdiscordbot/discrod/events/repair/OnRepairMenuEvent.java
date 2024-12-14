package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.repair.modals.RepairModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.Repair;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OnRepairMenuEvent extends ListenerAdapter {
    final
    MenuIdSet menuIdSet;
    final
    RepairModal repairModal;
    final
    Repair repair;
    final LivingRecordService lrService;
    final DiscordLinkService dcService;

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String eventMenuId = event.getSelectMenu().getId();
        if (eventMenuId == null || !event.getSelectMenu().getId().equalsIgnoreCase(menuIdSet.getRepair())) return;

        String selectedId = event.getSelectedOptions().get(0) == null ? "" :
                event.getSelectedOptions().get(0).getValue();

        if (selectedId.equalsIgnoreCase(menuIdSet.getInternetOption())) {
            event.deferReply().setEphemeral(true).queue();
            Optional<DiscordLink> dcLinkOption = dcService.findByDiscordId(event.getUser().getId());

            if (dcLinkOption.isEmpty()) {
                event.getHook().sendMessage(
                        "無驗證住宿生身份無法使用此功能"
                ).setEphemeral(true).queue();
                return;
            }

            DiscordLink dcLink = dcLinkOption.get();

            String studentId = dcLink.getStudent().getStudentId();
            Optional<LivingRecord> lrOptional = lrService.findByStudentId(studentId);
            if (lrOptional.isEmpty()) {
                event.getHook().sendMessage(
                        "非本學期住宿生無法使用此功能"
                ).setEphemeral(true).queue();
                return;
            }

            LivingRecord lr = lrOptional.get();
            String internetNumber = lr.getBed().getInternetNumber();

            event.getHook().sendMessage(String.format(
                    """
                    您好，%s
                    
                    因宿舍網路為中華電信承包，請您自行填寫中華電信「免登入」線上報修：
                    網址：[中華電信故障報修](https://search.app/pcjeCPUVtPBFp1vU6)
                    
                    免登入報修需填寫欄位：
                    - 房號 %s 的設備號碼：%s
                    - 統一編號後四碼：8413
                    """, dcLink.getStudent().getName(), lr.getBed().getBedId().substring(0, 4), internetNumber
            )).setEphemeral(true).queue();
            event.getHook().sendMessage(internetNumber).setEphemeral(true).queue();
            return;
        }

        event.replyModal(
                repairModal.getModal(
                        repair.getTypeByMenuId(selectedId)
                )
        ).queue();
    }
}
