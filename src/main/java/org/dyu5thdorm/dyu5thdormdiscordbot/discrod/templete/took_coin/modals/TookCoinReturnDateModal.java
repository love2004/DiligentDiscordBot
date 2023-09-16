package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.modals;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TookCoinReturnDateModal {
    Modal modal;
    @Autowired
    ModalIdSet modalIdSet;

    @PostConstruct
    void init() {
        modal = Modal.create(modalIdSet.getTookCoinReturn(), "廠商退費日期登記")
                .addComponents(
                        ActionRow.of(
                                TextInput.create(
                                        modalIdSet.getFirstTextInput(), "退費日期 格式：[西元年+月+日]", TextInputStyle.PARAGRAPH
                                ).setRequiredRange(8,8).setPlaceholder("""
                                        請輸入退費日期
                                        格式：[西元年+月+日]
                                        例如： 20230916
                                        """).build()
                        )
                )
                .build();
    }
}
