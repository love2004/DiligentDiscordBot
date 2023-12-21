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
                                            # 抽獎活動資訊：
                                            ### **報名資格：** 112-1 當前所有業勤住宿生，符合資格共 ***497*** 位
                                            ### **人數上限：**  ***500*** 位，額滿不再加開
                                            ### **報名截止日期：** *2023-12-27 19:59:59*
                                            ### **抽獎日期：** *2023-12-27 20:30:00*
                                            ### **抽獎方式：** Discord 抽獎頻道，時間到自動開獎
                                            ### **領獎辦法：**\s
                                              - 中獎者會在抽獎頻道公告被標註，一共三位，三位不同人。
                                              - 中獎者必須在三天內私訊**舍長**確認。
                                              - 可放棄獎項，會再抽出下一位
                                            ### 抽獎演算法：[hackmd.io](https://hackmd.io/@NUTT1101/Bkl8OPgPa)
                                            ### 重要說明： *主辦單位保有最終修改、變更、活動解釋及取消本活動之權利*
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
