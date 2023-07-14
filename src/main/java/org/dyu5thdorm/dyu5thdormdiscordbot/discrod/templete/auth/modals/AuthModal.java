package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.modals;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:discord.properties")
@Data
@Component
public class AuthModal {
    Modal modal;
    String modalId;
    String studentIdTextInputId;
    String phoneNumberTextInputId;

    @Value("${component.modal.auth}")
    public void setModalId(String modalId) {
        this.modalId = modalId;
    }

    @Value("${component.modal.auth-student-id-t}")
    public void setStudentIdTextInputId(String studentIdTextInputId) {
        this.studentIdTextInputId = studentIdTextInputId;
    }

    @Value("${component.modal.auth-phone-number-t}")
    public void setPhoneNumberTextInputId(String phoneNumberTextInputId) {
        this.phoneNumberTextInputId = phoneNumberTextInputId;
    }

    @PostConstruct
    public void init() {
        modal = Modal.create(modalId, "認證")
                .addActionRow(
                        TextInput.create(studentIdTextInputId, "學號(student id)", TextInputStyle.SHORT)
                                .setMinLength(8)
                                .setMaxLength(8)
                                .setRequired(true)
                                .setPlaceholder("F0000001")
                                .build()
                ).addActionRow(
                        TextInput.create(phoneNumberTextInputId, "手機電話號碼(phone number)", TextInputStyle.SHORT)
                                .setMinLength(10)
                                .setMaxLength(10)
                                .setRequired(true)
                                .setPlaceholder("0912345678")
                                .build()
                )
                .build();
    }
}

