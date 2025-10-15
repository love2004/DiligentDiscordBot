package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.RoleIdSet;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleOperation {
    final RoleIdSet roleIdSet;
    Map<Integer, String> floorRoleIdMap;

    @PostConstruct
    void initRole() {
        refreshFloorRoleMap();
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

    public void refreshFloorRoleMap() {
        Map<Integer, String> map = new HashMap<>();
        putIfPresent(map, 1, roleIdSet.getFloorOne());
        putIfPresent(map, 2, roleIdSet.getFloorTwo());
        putIfPresent(map, 3, roleIdSet.getFloorThree());
        putIfPresent(map, 4, roleIdSet.getFloorFour());
        putIfPresent(map, 5, roleIdSet.getFloorFive());
        putIfPresent(map, 6, roleIdSet.getFloorSix());
        floorRoleIdMap = Collections.unmodifiableMap(map);
    }

    private void putIfPresent(Map<Integer, String> target, int floor, String roleId) {
        if (roleId == null || roleId.isBlank()) return;
        target.put(floor, roleId);
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
