package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Maintenance {
    @Value("${mode.maintenance}")
    @Getter
    boolean maintenanceStatus;
    @Value("${role.dev}")
    String developerRole;

    public void setMaintenanceStatus(JDA jda, boolean maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
        update(jda);
    }

    public boolean isNotDeveloper(Member member) {
        return member.getRoles().stream().noneMatch(role -> developerRole.equalsIgnoreCase(role.getId()));
    }

    public void update(JDA jda) {
        if (maintenanceStatus) {
            jda.getPresence().setStatus(
                    OnlineStatus.DO_NOT_DISTURB
            );
            jda.getPresence().setActivity(
                    Activity.competing("維護中(In maintenance)")
            );
        } else {
            jda.getPresence().setStatus(
                    OnlineStatus.ONLINE
            );
            jda.getPresence().setActivity(
                    Activity.playing("正常運作")
            );
        }
    }
}
