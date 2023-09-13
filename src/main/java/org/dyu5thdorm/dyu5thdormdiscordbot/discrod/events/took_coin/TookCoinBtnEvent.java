package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ButtonIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.MenuIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ModalIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.embed.TookCoinEmbed;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.menu.TookCoinMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.modals.TookCoinModal;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.DiscordLink;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoin;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TookCoinBtnEvent extends ListenerAdapter {
    @Value("${regexp.money}")
    String moneySyntax;
    @Value("${regexp.floor}")
    String floorSyntax;
    @Value("${regexp.floor-and-area}")
    String floorAreaSyntax;
    @Value("${regexp.date}")
    String dateSyntax;

    @Autowired
    ButtonIdSet buttonIdSet;
    @Autowired
    MenuIdSet menuIdSet;
    @Autowired
    ModalIdSet modalIdSet;
    @Autowired
    ChannelIdSet channelIdSet;
    @Autowired
    TookCoinMenu menu;
    @Autowired
    TookCoinModal tookCoinModal;
    @Autowired
    TookCoin tookCoin;
    @Autowired
    DiscordLinkService discordLinkService;
    @Autowired
    TookCoinEmbed tookCoinEmbed;


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String eventButtonId = event.getButton().getId();
        if (!buttonIdSet.getTookCoin().equalsIgnoreCase(eventButtonId)) return;

        event.replyComponents(
                ActionRow.of(
                        menu.getMenu()
                )
        ).setEphemeral(true).queue();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String eventMenuId = event.getSelectMenu().getId();
        if (!menuIdSet.getTookCoin().equalsIgnoreCase(eventMenuId)) return;

        String selectedOptionId = event.getSelectedOptions().get(0).getValue();
        TookCoin.Type reportType = getTypeByMenuId(selectedOptionId);
        if (reportType == null) {
            return;
        }

        event.replyModal(
                tookCoinModal.getModalByType(reportType)
        ).queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String eventModalId = event.getModalId();
        if (!List.of(
                modalIdSet.getTookCoinDryer(),
                modalIdSet.getTookCoinVending(),
                modalIdSet.getTookCoinWashMachine()
        ).contains(eventModalId)) return;

        List<String> args = event.getValues().stream().map(ModalMapping::getAsString).toList();

        boolean isWashOrDry = eventModalId.equalsIgnoreCase(modalIdSet.getTookCoinDryer())
                || eventModalId.equalsIgnoreCase(modalIdSet.getTookCoinWashMachine());

        if (isWashOrDry && !args.get(0).matches(floorAreaSyntax)) {
            event.replyEmbeds(
                    tookCoinEmbed.getBySyntaxWrong(TookCoinEmbed.SyntaxWrong.FLOOR_AREA).build()
            ).setEphemeral(true).queue();
            return;
        } else if (!isWashOrDry && !args.get(0).matches(floorSyntax)) {
            event.replyEmbeds(
                    tookCoinEmbed.getBySyntaxWrong(TookCoinEmbed.SyntaxWrong.FLOOR).build()
            ).setEphemeral(true).queue();
            return;
        }

        if (!args.get(2).matches(moneySyntax)) {
            event.replyEmbeds(
                    tookCoinEmbed.getBySyntaxWrong(TookCoinEmbed.SyntaxWrong.MONEY).build()
            ).setEphemeral(true).queue();
            return;
        }

        if (!args.get(3).matches(dateSyntax)) {
            event.replyEmbeds(
                    tookCoinEmbed.getBySyntaxWrong(TookCoinEmbed.SyntaxWrong.DATE).build()
            ).setEphemeral(true).queue();
            return;
        }

        DiscordLink discordLink = discordLinkService.findByDiscordId(event.getUser().getId());
        if (discordLink == null) {
            event.reply("未綁定住宿生身分無法進行此動作！").setEphemeral(true).queue();
            return;
        }

        TookCoin.Type type = getTypeByModalId(eventModalId);

        TookCoin.FailReason r = tookCoin.record(type, args, discordLink.getStudent());

        if (r != TookCoin.FailReason.NONE) {
            event.replyEmbeds(
                    tookCoinEmbed.getByReason(r).build()
            ).setEphemeral(true).queue();
            return;
        }

        event.reply("登記成功").setEphemeral(true).queue();

        TextChannel textChannel = event.getJDA().getTextChannelById(channelIdSet.getTookCoinCadre());
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getUser().getId(), args, discordLink);
        if (textChannel == null) {
            return;
        }
        textChannel.sendMessageEmbeds(
                embedBuilder.build()
        ).queue();
    }

    @NotNull
    private static EmbedBuilder getEmbedBuilder(String userId, List<String> args, DiscordLink discordLink) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("有新的一筆吃錢登記");
        embedBuilder.setDescription(
                String.format("<@%s> 登記資訊如下", userId)
        );
        embedBuilder.addField("發生地點", args.get(0), true);
        embedBuilder.addField("故障情況說明", args.get(1), true);
        embedBuilder.addField("卡幣金額", args.get(2), true);
        embedBuilder.addField("發生時間", args.get(3), true);
        embedBuilder.addField("回報者學號", discordLink.getStudent().getStudentId(), true);
        embedBuilder.addField("回報者姓名", discordLink.getStudent().getName(), true);
        return embedBuilder;
    }


    TookCoin.Type getTypeByMenuId(String id) {
        if (menuIdSet.getVendingOption().equalsIgnoreCase(id)) {
            return TookCoin.Type.VENDING;
        } else if (menuIdSet.getDryerOption().equalsIgnoreCase(id)) {
            return TookCoin.Type.DRYER;
        } else if (menuIdSet.getWashingMachineOption().equalsIgnoreCase(id)) {
            return TookCoin.Type.WASH_MACHINE;
        } else return null;
    }

    TookCoin.Type getTypeByModalId(String id) {
        if (modalIdSet.getTookCoinVending().equalsIgnoreCase(id)) {
            return TookCoin.Type.VENDING;
        } else if (modalIdSet.getTookCoinDryer().equalsIgnoreCase(id)) {
            return TookCoin.Type.DRYER;
        } else if (modalIdSet.getTookCoinWashMachine().equalsIgnoreCase(id)) {
            return TookCoin.Type.WASH_MACHINE;
        } else return null;
    }
}
