package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.req_lev.modals;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReqLevModal {
    @Autowired
    ModalIdSet modalIdSet;

    Modal.Builder modal;

    @PostConstruct
    void init() {
        modal = Modal.create(modalIdSet.getReqForLeave(), "晚間點名請假")
                .addComponents(
                        ActionRow.of(
                                TextInput.create(modalIdSet.getFirstTextInput(), "請假事由(Reason)", TextInputStyle.PARAGRAPH)
                                        .setMaxLength(50)
                                        .setPlaceholder("請填寫請假事由。")
                                        .build()
                        )
                );
    }

    public Modal getModal() {
        return modal.setTitle(
                LocalDate.now() + " 晚間點名請假"
        ).build();
    }
}
