package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.embeds.AttendanceEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto.AttendanceDTO;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.AttendanceService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AttendancePersonalCheckEvent extends ListenerAdapter {
    final ButtonIdSet buttonIdSet;
    final LivingRecordService lrService;
    final AttendanceService aService;
    final AttendanceEmbedBuilder attendanceEmbedBuilder;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!buttonIdSet.getAttendanceRate().equalsIgnoreCase(event.getButton().getId())) return;

        event.deferReply().setEphemeral(true).queue();

        String discordId = event.getUser().getId();
        Optional<LivingRecord> livingRecordOptional = lrService.findLivingRecordByDiscordId(discordId);
        if (livingRecordOptional.isEmpty()) {
            event.getHook().sendMessage("""
                    > 非本學期住宿生無法使用此功能。
                    """).queue();
            return;
        }

        Student student = livingRecordOptional.get().getStudent();
        Set<AttendanceDTO> records = aService.findTop5AttendanceRecord(student);
        EmbedBuilder embedBuilder = attendanceEmbedBuilder.getPersonalCheck(records, livingRecordOptional.get());

        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
