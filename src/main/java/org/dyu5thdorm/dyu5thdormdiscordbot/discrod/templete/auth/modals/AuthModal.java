package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.modals;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class AuthModal {
    Modal modal;
    final
    ModalIdSet modalIdSet;

    @PostConstruct
    void init() {
        modal = Modal.create(modalIdSet.getAuth(), "認證")
                .addActionRow(
                        TextInput.create(modalIdSet.getFirstTextInput(), "學號(student id)", TextInputStyle.SHORT)
                                .setRequiredRange(8,8)
                                .setRequired(true)
                                .setPlaceholder("F0000001")
                                .build()
                ).addActionRow(
                        TextInput.create(modalIdSet.getSecondTextInput(), "手機電話號碼(phone number)", TextInputStyle.SHORT)
                                .setRequiredRange(10,10)
                                .setRequired(true)
                                .setPlaceholder("0912345678")
                                .build()
                )
                .build();
    }
}

