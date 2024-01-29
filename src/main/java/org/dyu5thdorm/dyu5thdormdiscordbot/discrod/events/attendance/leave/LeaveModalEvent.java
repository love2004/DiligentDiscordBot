package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.leave;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ReqLevOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LeaveRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LeaveModalEvent extends ListenerAdapter {
    final
    ModalIdSet modalIdSet;
    final
    LeaveRecordService leaveService;
    final
    LivingRecordService livingRecordService;
    final
    ReqLevOperation reqLevOperation;


    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!modalIdSet.getReqForLeave().equalsIgnoreCase(eventModalId)) return;
        event.deferReply().setEphemeral(true).queue();

        if (reqLevOperation.isIllegalTime()) {
            event.getHook().sendMessage(
                    String.format("""
                    > 已超過點名請假\\\\補點名時間。點名請假\\\\補點名時間為每天的 %s ~ %s。
                    """, reqLevOperation.getStartTime(), reqLevOperation.getEndTime())
            ).setEphemeral(true).queue();
            return;
        }

        String reason = event.getValues().get(0).getAsString();

        Optional<LivingRecord> record = livingRecordService.findLivingRecordByDiscordId(
                event.getUser().getId()
        );

        if (record.isEmpty()) {
            event.getHook().sendMessage("> 無法使用此功能，因為您非本學期之住宿生！").setEphemeral(true).queue();
            return;
        }

        leaveService.save(record.get(), reason);
        event.getHook().sendMessage("""
                > 提交成功！
                """).setEphemeral(true).queue();
    }
}
