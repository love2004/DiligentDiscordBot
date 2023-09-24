package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.req_lev;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.ReqLevOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LeaveTempRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.stereotype.Component;

@Component
public class ReqLevModalEvent extends ListenerAdapter {
    final
    ModalIdSet modalIdSet;
    final
    LeaveTempRecordService leaveService;
    final
    LivingRecordService livingRecordService;
    final
    ReqLevOperation reqLevOperation;

    public ReqLevModalEvent(ModalIdSet modalIdSet, LeaveTempRecordService leaveService, LivingRecordService livingRecordService, ReqLevOperation reqLevOperation) {
        this.modalIdSet = modalIdSet;
        this.leaveService = leaveService;
        this.livingRecordService = livingRecordService;
        this.reqLevOperation = reqLevOperation;
    }


    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!modalIdSet.getReqForLeave().equalsIgnoreCase(eventModalId)) return;
        event.deferReply().setEphemeral(true).queue();

        if (reqLevOperation.isIllegalTime()) {
            event.getHook().sendMessage(
                    String.format("""
                    > 已超過點名請假時間。點名請假時間為每天的 00:00 ~ %d:%d。
                    """, reqLevOperation.getAvailableTimeHour(), reqLevOperation.getAvailableTimeMinute())
            ).setEphemeral(true).queue();
            return;
        }

        String reason = event.getValues().get(0).getAsString();

        LivingRecord record = livingRecordService.findLivingRecordByDiscordId(
                event.getUser().getId()
        );

        if (record == null) {
            event.getHook().sendMessage("> 無法使用此功能，因為您非本學期之住宿生！").setEphemeral(true).queue();
            return;
        }

        leaveService.save(record.getBed(), record.getStudent(), reason);
        event.getHook().sendMessage("""
                > 請假申請成功！
                """).setEphemeral(true).queue();
    }
}
