package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils.RoleOperation;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FloorRoleCorBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    @Autowired
    LivingRecordService livingRecordService;
    @Autowired
    RoleOperation roleOperation;

    public FloorRoleCorBtn(ButtonIdSet buttonIdSet) {
        this.buttonIdSet = buttonIdSet;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getFloorRoleCorrection().equalsIgnoreCase(eventButtonId)) return;

        event.deferReply().setEphemeral(true).queue();

        Guild thisGuild = event.getGuild();

        if (thisGuild == null) {
            event.getHook().sendMessage("找不到伺服器").setEphemeral(true).queue();
            return;
        }

        List<Member> members = thisGuild.getMembers();

        for (Member member : members) {
            LivingRecord record = livingRecordService.findLivingRecordByDiscordId(member.getUser().getId());
            if (record == null) continue;

            int floor = roleOperation.getFloorByBedId(record.getBed().getBedId());
            String correctRoleId = roleOperation.getRoleIdByFloor(floor);
            Role role = thisGuild.getRoleById(correctRoleId);
            if (role == null) return;
            if (!member.getRoles().contains(role)) {
                roleOperation.removeAllFloorRoles(thisGuild, member);
                thisGuild.addRoleToMember(member, role).queue();
                event.getHook().sendMessage(
                        String.format(
                                "<@%s> 成功校正=> <@&%s>", member.getUser().getId(), correctRoleId
                        )
                ).setEphemeral(true).queue();
            }
        }

        event.getHook().sendMessage("校正完畢").setEphemeral(true).queue();
    }
}
