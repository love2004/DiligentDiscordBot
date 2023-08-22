package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Maintenance {
    @Value("${mode.maintenance}")
    boolean maintenanceStatus;
    @Autowired
    @Setter(AccessLevel.NONE)
    DiscordAPI api;

    public void setMaintenanceStatus(boolean maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
        update();
    }

    public void update() {
        if (maintenanceStatus) {
            api.getJda().getPresence().setStatus(
                    OnlineStatus.DO_NOT_DISTURB
            );
            api.getJda().getPresence().setActivity(
                    Activity.playing("目前正在維修中")
            );
        } else {
            api.getJda().getPresence().setStatus(
                    OnlineStatus.ONLINE
            );
        }
    }
}
