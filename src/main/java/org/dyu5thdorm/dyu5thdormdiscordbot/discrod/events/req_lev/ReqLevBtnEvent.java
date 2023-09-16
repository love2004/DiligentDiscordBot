package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.req_lev;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.req_lev.modals.ReqLevModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LeaveTempRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.NoCallRollDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@PropertySource("classpath:req_leave.properties")
public class ReqLevBtnEvent extends ListenerAdapter {
    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    NoCallRollDateService noCallRollDateService;
    @Autowired
    LeaveTempRecordService leaveTempRecordService;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    ReqLevModal reqLevModal;
    @Value("${available.time.hour}")
    Integer availableTimeHour;
    @Value("${available.time.minute}")
    Integer availableTimeMinute;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getReqForLeave().equalsIgnoreCase(eventButtonId)) return;

        LocalDate now = LocalDate.now();
        if (noCallRollDateService.exists(now)) {
            event.reply("""
                    今天無須點名！
                    > No roll call today!
                    """).setEphemeral(true).queue();
            return;
        }

        LivingRecord livingRecord = livingRecordService.findLivingRecordByDiscordId(event.getUser().getId());
        if (livingRecord == null) {
            event.reply("""
                    非本學期住宿生無法使用此功能。
                    > Only current semester residents can use this feature.
                    """).setEphemeral(true).queue();
            return;
        }

        Student student = livingRecord.getStudent();
        if (leaveTempRecordService.isRequested(livingRecord.getBed().getBedId(), student.getStudentId(), now)) {
            event.reply("""
                    您今天已提交過請假申請！
                    > You have already submitted a leave request today!
                    """).setEphemeral(true).queue();
            return;
        }

        if (isIllegalTime(LocalDateTime.now())) {
            event.reply(
                    String.format("""
                    已超過點名請假時間。點名請假時間為每天的 00:00 ~ %d:%d。
                    > You have exceeded the late leave request time. Please make your leave request within the specified time frame next time.
                    """, availableTimeHour, availableTimeMinute)
            ).setEphemeral(true).queue();
            return;
        }

        event.replyModal(reqLevModal.getModal()).queue();
    }

    boolean isIllegalTime(LocalDateTime time) {
        return time.getHour() >= availableTimeHour && time.getMinute() >= availableTimeMinute;
    }
}
