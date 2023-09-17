package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.req_lev;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LeaveTempRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReqLevModalEvent extends ListenerAdapter {
    @Autowired
    ModalIdSet modalIdSet;
    @Autowired
    LeaveTempRecordService leaveService;
    @Autowired
    LivingRecordService livingRecordService;


    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!modalIdSet.getReqForLeave().equalsIgnoreCase(eventModalId)) return;
        event.deferReply().setEphemeral(true).queue();
        String reason = event.getValues().get(0).getAsString();

        LivingRecord record = livingRecordService.findLivingRecordByDiscordId(
                event.getUser().getId()
        );

        if (record == null) {
            event.getHook().sendMessage("無法使用此功能，因為您非本學期之住宿生！").setEphemeral(true).queue();
            return;
        }

        leaveService.save(record.getBed(), record.getStudent(), reason);
        event.getHook().sendMessage("""
                請假申請成功！
                > Leave application successful!
                """).setEphemeral(true).queue();
    }
}
