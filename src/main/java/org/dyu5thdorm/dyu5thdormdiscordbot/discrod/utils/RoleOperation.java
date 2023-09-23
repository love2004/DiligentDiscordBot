package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.RoleIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RoleOperation {
    @Autowired
    RoleIdSet roleIdSet;
    Map<Integer, String> floorRoleIdMap;

    @PostConstruct
    void initRole() {
        floorRoleIdMap = Map.of(
                1, roleIdSet.getFloorOne(),
                2, roleIdSet.getFloorTwo(),
                3, roleIdSet.getFloorThree(),
                4, roleIdSet.getFloorFour(),
                5, roleIdSet.getFloorFive(),
                6, roleIdSet.getFloorSix()
        );
    }

    public int getFloorByBedId(String bedId) {
        char floor = bedId.charAt(1);
        return Character.getNumericValue(floor);
    }

   public void addRoleToMemberByFloor(Guild guild, Member member, String bedId) {
        String roleId = this.getRoleIdByFloor(
                this.getFloorByBedId(bedId)
        );

        Role role = guild.getRoleById(roleId);
        if (role == null) return;
        guild.addRoleToMember(member, role).queue();
    }

    public String getRoleIdByFloor(int floor) {
        return this.floorRoleIdMap.getOrDefault(floor, null);
    }

    public void removeAllRoles(Guild guild, Member member) {
        member.getRoles().forEach(
                role -> guild.removeRoleFromMember(member, role).queue()
        );
    }

    public void removeAllFloorRoles(Guild guild, Member member) {
        for (Role role : member.getRoles()) {
            if (!this.floorRoleIdMap.containsValue(role.getId())) continue;
            guild.removeRoleFromMember(member, role).queue();
        }
    }
}
