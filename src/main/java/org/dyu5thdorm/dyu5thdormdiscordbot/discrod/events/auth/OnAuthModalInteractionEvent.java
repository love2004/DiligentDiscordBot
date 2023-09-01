package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.RoleIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.embeds.AuthEmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OnAuthModalInteractionEvent extends ListenerAdapter {
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    DiscordLinkService discordLinkService;
    @Autowired
    RoleIdSet roleIdSet;
    @Autowired
    StudentService studentService;
    @Autowired
    AuthEmbedBuilder authEmbedBuilder;
    @Value("${regexp.phone_number}")
    String phoneNumberSyntax;
    @Autowired
    ModalIdSet modalIdSet;
    @Autowired
    ChannelIdSet channelIdSet;

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals(modalIdSet.getAuth())) return;

        String userId = event.getUser().getId();

        // Will not be null value.
        String studentId = event.getValue(modalIdSet.getFirstTextInput()).getAsString().toUpperCase();
        String phoneNumber = event.getValue(modalIdSet.getSecondTextInput()).getAsString();

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
            event.reply("無法將您的資料對應到本學期之床位，您可能非本學期住宿生或已離宿，若有任何問題請聯繫宿舍幹部。").setEphemeral(true).queue();
            return;
        }

        String bedId = livingRecordFound.get().getBed().getBedId();

        discordLinkService.link(event.getUser().getId(), studentId);
        studentService.saveStudentPhoneNumber(studentId, phoneNumber);

        event.reply("恭喜！您已通過驗證。若有任何問題請聯繫宿舍幹部。").setEphemeral(true).queue();

        if (event.getGuild() == null) {
            return;
        }

        giveFloorRoleToUser(event.getGuild(), event.getUser(), bedId);

        LivingRecord livingRecord = livingRecordFound.get();

        EmbedBuilder embedBuilder = authEmbedBuilder.successAuth(livingRecord);
        event.getHook().sendMessageEmbeds(
                embedBuilder.build()
        ).setEphemeral(true).queue();
        TextChannel logger = event.getJDA().getTextChannelById(channelIdSet.getAuthLogger());
        if (logger != null) {
            logger.sendMessageEmbeds(
                    authEmbedBuilder.successAuthLogger(embedBuilder, userId).build()
            ).queue();
        }
    }

    void giveFloorRoleToUser(Guild guild, User user, String bedId) {
        int floor = (bedId.charAt(1) - '0');
        String roleId = roleIdSet.getRoleIdByFloor(floor);
        Role role = guild.getRoleById(roleId);
        if (role == null) {
            // TODO: HANDLE
            return;
        }
        guild.addRoleToMember(user, role).queue();
    }
}
