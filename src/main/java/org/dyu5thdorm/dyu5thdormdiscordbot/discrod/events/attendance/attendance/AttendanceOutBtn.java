package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.springframework.stereotype.Component;

@Component
public class AttendanceOutBtn extends ListenerAdapter {
    final
    ButtonIdSet buttonIdSet;
    final
    AttendanceHandler attendanceHandler;

    public AttendanceOutBtn(ButtonIdSet buttonIdSet, AttendanceHandler attendanceHandler) {
        this.buttonIdSet = buttonIdSet;
        this.attendanceHandler = attendanceHandler;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getAttendanceOut().equalsIgnoreCase(eventButtonId)) return;


    }
}
