package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.attendace.modals;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LeaveModal {
    final
    ModalIdSet modalIdSet;

    Modal.Builder modal;

    @PostConstruct
    void init() {
        modal = Modal.create(modalIdSet.getReqForLeave(), "晚間點名請假\\補點名")
                .addComponents(
                        ActionRow.of(
                                TextInput.create(modalIdSet.getFirstTextInput(), "事由(Reason)", TextInputStyle.PARAGRAPH)
                                        .setMaxLength(50)
                                        .setPlaceholder("請填寫事由。")
                                        .build()
                        )
                );
    }

    public Modal getModal() {
        return modal.setTitle(
                String.valueOf(LocalDate.now())
        ).build();
    }
}
