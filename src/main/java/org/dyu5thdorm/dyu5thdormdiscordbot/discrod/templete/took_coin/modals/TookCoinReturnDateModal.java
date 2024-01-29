package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.modals;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class TookCoinReturnDateModal {
    Modal modal;
    final
    ModalIdSet modalIdSet;

    @Value("${date-no-line.format}")
    String datePattern;
    TextInput.Builder textInput;
    @PostConstruct
    void init() {
        textInput = TextInput.create(
                modalIdSet.getFirstTextInput(), "退費日期 格式：[西元年+月+日]", TextInputStyle.PARAGRAPH
        ).setRequiredRange(8,8).setPlaceholder(
                """
                請輸入退費日期
                格式：[西元年+月+日]
                例如： 20230916
                """
        );
        modal = Modal.create(modalIdSet.getTookCoinReturn(), "廠商退費日期登記")
                .addComponents(
                        ActionRow.of(
                                textInput.build()
                        )
                )
                .build();
    }

    public Modal getModal() {
        LocalDate today = LocalDate.now();
        Modal.Builder modalCopy = modal.createCopy();
        TextInput.Builder textInputCopy = textInput;
        modalCopy.getComponents().get(0).updateComponent(
                modalIdSet.getFirstTextInput(),
                textInputCopy.setValue(
                        DateTimeFormatter.ofPattern(datePattern).format(today)
                ).build()
        );
        return modalCopy.build();
    }
}
