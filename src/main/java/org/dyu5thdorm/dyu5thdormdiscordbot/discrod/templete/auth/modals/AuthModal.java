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
    Modal defaultModal, phoneModal, mailModal;
    final
    ModalIdSet modalIdSet;

    @PostConstruct
    void init() {
        defaultModal = Modal.create(modalIdSet.getAuth(), "認證(Verify)")
                .addActionRow(
                        TextInput.create(modalIdSet.getFirstTextInput(), "學號(student id)", TextInputStyle.SHORT)
                                .setRequiredRange(8,8)
                                .setRequired(true)
                                .setPlaceholder("F0000001")
                                .build()
                ).build();

        phoneModal = defaultModal
                .createCopy()
                .setId(modalIdSet.getAuthPhone())
                .addActionRow(
                        TextInput.create(modalIdSet.getSecondTextInput(), "臺灣手機號碼(Taiwan phone number)", TextInputStyle.SHORT)
                                .setRequiredRange(10,10)
                                .setRequired(true)
                                .setPlaceholder("0912345678")
                                .build()
                )
                .build();

        mailModal = defaultModal
                .createCopy()
                .setId(modalIdSet.getAuthMail())
                .addActionRow(
                        TextInput.create(modalIdSet.getSecondTextInput(), "電子郵件(Email)", TextInputStyle.SHORT)
                                .setMaxLength(254)
                                .setRequired(true)
                                .setPlaceholder("hello@dyu5thdorm.org")
                                .build()
                )
                .build();
    }
}

