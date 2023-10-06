package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.attendance.attendance;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dyu5thdorm.dyu5thdormdiscordbot.attendance.AttendanceHandler;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.springframework.stereotype.Component;

@Component
public class AttendanceOutModal extends ListenerAdapter {
    final
    AttendanceHandler attendanceHandler;
    final
    AttendanceEventUtils attendanceEventUtils;
    final
    ModalIdSet modalIdSet;

    public AttendanceOutModal(AttendanceHandler attendanceHandler, AttendanceEventUtils attendanceEventUtils, ModalIdSet modalIdSet) {
        this.attendanceHandler = attendanceHandler;
        this.attendanceEventUtils = attendanceEventUtils;
        this.modalIdSet = modalIdSet;
    }


    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!modalIdSet.getAttendanceOut().equalsIgnoreCase(eventModalId)) return;

        String roomId = event.getValue(modalIdSet.getFirstTextInput()).getAsString();

    }
}
