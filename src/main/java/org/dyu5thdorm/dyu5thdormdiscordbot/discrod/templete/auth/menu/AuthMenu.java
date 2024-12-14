package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.auth.menu;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMenu {
    @Getter
    StringSelectMenu selectMenu;
    final MenuIdSet menuIdSet;
    final ModalIdSet modalIdSet;

    @PostConstruct
    void setup() {
        this.selectMenu = StringSelectMenu.create(
                menuIdSet.getAuth()
        ).setRequiredRange(1,1)
                .addOption(
                    "電話號碼(phone number of Taiwan)",
                    modalIdSet.getFirstTextInput(),
                    "Verify using a Taiwan phone number",
                    Emoji.fromUnicode("U+1F4DE")
            )
            .addOption(
                    "電子郵件(email)",
                    modalIdSet.getSecondTextInput(),
                    "Verify using a email",
                    Emoji.fromUnicode("U+2709")
            )
            .build();
    }
}
