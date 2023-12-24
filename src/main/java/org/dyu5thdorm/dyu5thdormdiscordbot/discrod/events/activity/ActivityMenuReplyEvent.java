package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.activity;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ActivityMenuReplyEvent extends ListenerAdapter {
    final
    MenuIdSet menuIdSet;
    final
    ButtonIdSet buttonIdSet;

    public ActivityMenuReplyEvent(MenuIdSet menuIdSet, ButtonIdSet buttonIdSet) {
        this.menuIdSet = menuIdSet;
        this.buttonIdSet = buttonIdSet;
    }

    // TODO: 回覆一個EntitySelectionMenu給予使用者選取要投放在何頻道，且具有「參加」、「查看資訊」、「取消參加」等按鈕，其中查看資訊action可以自訂。
    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        if (!menuIdSet.getActivity().equalsIgnoreCase(event.getSelectMenu().getId())) return;
        event.deferReply().setEphemeral(true).queue();
        event.getMentions().getChannels().forEach(
                guildChannel -> {
                    if (!(guildChannel instanceof TextChannel tc)) return;

                    tc.sendMessage("""
                                    # 活動說明
                                    ## 獎品內容及抽獎演算法：[hackmd.io](https://hackmd.io/@NUTT1101/Bkl8OPgPa)
                                    ### 參與資格： 僅限於 112-1 當前的業勤住宿生，共 497 人符合條件
                                    ### 人數上限： 500 位，額滿不再開放
                                    ### 報名截止日期： 2023-12-27 19:59:59
                                    ### 抽獎日期： 2023-12-27 20:30:00
                                    ### 抽獎方式： 在 Discord 抽獎頻道進行，開獎時間一到將自動進行
                                    ### 領獎辦法：
                                    - 中獎者名單將在抽獎頻道公布並被標註，共有七位不同的中獎者。
                                        - 這七位中獎者將依照中獎順序，從獎品中選擇最多一項，或選擇放棄。
                                        - 若經過兩輪選擇後仍有剩餘獎品（稱為“輪空”），則視為無人領取，獎品將被保留，不再抽出。
                                    - 中獎者需在三天內私訊舍長確認領獎。
                                    - 如放棄領獎，將重新抽取下一位中獎者，直至兩次輪空條件滿足。
                                    ### 重要說明： 主辦單位保留對活動規則進行最終解釋、修改、變更或取消的權利。
                                    """)
                            .addComponents(
                                    ActionRow.of(
                                            Button.success(buttonIdSet.getActivityParticipate(), "參加\\查看資訊"),
                                            Button.danger(buttonIdSet.getActivityCancel(), "取消參加")
                                    )
                            ).queue();
                }
        );
        event.getHook().sendMessage("ＯＫ").setEphemeral(true).queue();
    }
}
