package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.req_lev.cadre;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LeaveTempRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LeaveTempRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ReqLevCadreBtnEvent extends ListenerAdapter {
    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    LeaveTempRecordService leaveTempRecordService;
    @Value("${datetime.format}")
    String dateFormat;
    DateTimeFormatter dateTimeFormatter;

    @PostConstruct
    void init() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getReqForLeaveCadre().equalsIgnoreCase(eventButtonId)) return;

        LivingRecord record = livingRecordService.findLivingRecordByDiscordId(event.getUser().getId());

        if (record == null) {
            event.reply("你無法使用此功能").setEphemeral(true).queue();
            return;
        }

        Integer floor = record.getBed().getBedId().charAt(1) - '0';
        var r = leaveTempRecordService.findAllByFloorAndDate(
                floor, LocalDate.now()
        );

        boolean isCd = getRoomId(record.getBed().getBedId()) < 21;
        event.reply(
                getArea(floor, isCd) +
                        String.format(
                                r.isEmpty() ? " 查無請假申請紀錄" : " 查詢成功(若無顯示則為無人請假)。查詢日期為：%s", LocalDate.now()
                        )
        ).setEphemeral(true).queue();

        for (LeaveTempRecord e : r) {
            Integer roomId = getRoomId(e.getBed().getBedId());
            if (isCd && roomId >= 20 || !isCd && roomId < 20) continue;

            EmbedBuilder embedBuilder = getEmbedBuilder(floor, e, isCd);

            event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }

    @NotNull
    private EmbedBuilder getEmbedBuilder(Integer floor, LeaveTempRecord e, boolean isCd) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getArea(floor, isCd));
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.addField("房號", e.getBed().getBedId(), true);
        embedBuilder.addField("學號", e.getStudent().getStudentId(), true);
        embedBuilder.addField("姓名", e.getStudent().getName(), true);
        embedBuilder.addField("請假原因", e.getReason(), true);
        embedBuilder.addField("請假時間", e.getRequestTime().format(dateTimeFormatter), true);
        return embedBuilder;
    }

    Integer getRoomId(String bedId) {
        String bedIdStr = bedId.substring(2, 4);
        return Integer.parseInt(bedIdStr);
    }

    String getArea(Integer floor, boolean isCd) {
        return floor + (isCd ? "樓CD區" : "樓AB區");
    }
}
