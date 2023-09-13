package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.RoleIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleOperation {
    @Autowired
    RoleIdSet roleIdSet;

    public int getFloorByBedId(String bedId) {
        char floor = bedId.charAt(1);
        return (floor - '0');
    }

   public void addRoleToMemberByFloor(Guild guild, Member member, String bedId) {
        String roleId = roleIdSet.getRoleIdByFloor(
                this.getFloorByBedId(bedId)
        );

        Role role = guild.getRoleById(roleId);
        if (role == null) return;
        guild.addRoleToMember(member, role).queue();
    }

    public String getRoleIdByFloor(int floor) {
        return roleIdSet.getRoleIdByFloor(floor);
    }

    public void removeAllRoles(Guild guild, Member member) {
        member.getRoles().forEach(
                role -> guild.removeRoleFromMember(member, role).queue()
        );
    }

    public void removeAllFloorRoles(Guild guild, Member member) {
        for (Role role : member.getRoles()) {
            if (!roleIdSet.getFloorsRole().containsValue(role.getId())) continue;
            guild.removeRoleFromMember(member, role).queue();
        }
    }
}
