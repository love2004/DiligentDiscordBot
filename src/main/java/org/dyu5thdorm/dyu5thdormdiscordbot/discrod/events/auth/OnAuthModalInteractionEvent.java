package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordRole;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Optional;

@Component
@PropertySource("classpath:discord.properties")
public class OnAuthModalInteractionEvent extends ListenerAdapter {
    @Value("${regexp.phone_number}")
    String phoneNumberSyntax;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    DiscordLinkService discordLinkService;
    @Autowired
    DiscordRole discordRole;
    @Autowired
    StudentService studentService;
    @Value("${component.modal.auth}")
    String modalId;
    @Value("${component.modal.auth-student-id-t}")
    String modalStudentId;
    @Value("${component.modal.auth-phone-number-t}")
    String modalPhoneNumber;

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals(modalId)) return;

        String userId = event.getUser().getId();

        // Will not be null value.
        String studentId = event.getValue(modalStudentId).getAsString().toUpperCase();
        String phoneNumber = event.getValue(modalPhoneNumber).getAsString();

        if (phoneNumber.isEmpty() || studentId.isEmpty()) {
            event.reply("請輸入學號及電話號碼").setEphemeral(true).queue();
            return;
        }

        if (!phoneNumber.matches(phoneNumberSyntax)) {
            event.reply("請輸入正確格式的電話號碼。").setEphemeral(true).queue();
            return;
        }

        if (discordLinkService.isLinked(userId)) {
            event.reply("您已完成驗證，無需再次提交驗證請求。").setEphemeral(true).queue();
            return;
        }

        if (!studentService.exists(studentId)) {
            event.reply("無法驗證您所輸入的資料，請確認格式以及資料正確。").setEphemeral(true).queue();
            return;
        }

        if (discordLinkService.isLinkByStudentId(studentId)) {
            event.reply("您所輸入的資料已被其他住宿生綁定！若有任何問題請聯繫宿舍幹部。").setEphemeral(true).queue();
            return;
        }

        Optional<LivingRecord> livingRecordFound = livingRecordService.findByStudentId(studentId);
        if (livingRecordFound.isEmpty()) {
            event.reply("無法將您的資料對應到相對床位，您可能非本學期住宿生或已離宿，若有任何問題請聯繫宿舍幹部。").queue();
            return;
        }

        String bedId = livingRecordFound.get().getBed().getBedId();

        discordLinkService.link(event.getUser().getId(), studentId);
        studentService.saveStudentPhoneNumber(studentId, phoneNumber);

        event.reply("恭喜！您已通過驗證。您的床位資料已發送私訊給您，若有任何問題請聯繫宿舍幹部。").setEphemeral(true).queue();

        if (event.getGuild() == null) {
            return;
        }

        giveFloorRoleToUser(event.getGuild(), event.getUser(), bedId);

        LivingRecord livingRecord = livingRecordFound.get();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("住宿資料")
                .setDescription("以下為您的住宿資料，若有誤或非本人請立刻與宿舍幹部反應。")
                .addField("房號", livingRecord.getBed().getBedId(), true)
                .addField("學號", livingRecord.getStudent().getStudentId(), true)
                .addField("姓名", livingRecord.getStudent().getName(), true)
                .setColor(Color.GREEN);
        event.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    void giveFloorRoleToUser(Guild guild, User user, String bedId) {
        int floor = (bedId.charAt(1) - '0');
        String roleId = discordRole.getRoleIdByFloor(floor);
        Role role = guild.getRoleById(roleId);
        if (role == null) {
            // TODO: HANDLE
            return;
        }

        guild.addRoleToMember(user, role).queue();
    }
}
