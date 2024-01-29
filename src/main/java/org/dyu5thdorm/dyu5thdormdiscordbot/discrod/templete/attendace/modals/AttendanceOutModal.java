package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.modals;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceOutModal {
    final
    ModalIdSet modalIdSet;

    public Modal getModal(@NotNull String roomId) {
        return Modal.create(modalIdSet.getAttendanceOut(), "晚間點名缺席登記").addComponents(
                ActionRow.of(
                        TextInput.create(modalIdSet.getFirstTextInput(), "房號(此欄位勿改)", TextInputStyle.SHORT)
                                .setValue(roomId)
                                .setRequiredRange(4,4)
                                .build()
                ),
                ActionRow.of(
                        TextInput.create(modalIdSet.getSecondTextInput(), "缺席床位", TextInputStyle.SHORT)
                                .setRequiredRange(1, 4)
                                .setPlaceholder("輸入格式(床位)：A 或 ACD 或 acd")
                                .build()
                )
        ).build();
    }
}
