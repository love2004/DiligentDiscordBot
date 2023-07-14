package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.modals.RepairModals;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.LineNotify;
import org.dyu5thdorm.dyu5thdormdiscordbot.line.RepairTokenSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.DiscordLinkService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@PropertySource("classpath:discord.properties")
public class RepairRequestEvent extends ListenerAdapter {
    final RepairModals repairModals;
    final LineNotify lineNotify;
    final LivingRecordService livingRecordService;
    final DiscordLinkService discordLinkService;
    @Value("${component.button.repair}")
    String repairButtonId;
    @Value("${component.menu.repair}")
    String repairMenuId;
    @Value("${component.menu.option-value-normal}")
    String menuNormalOptionValue;
    @Value("${component.menu.option-value-wash-and-dry}")
    String menuWashAndDyuOptionValue;
    @Value("${component.menu.option-value-vending}")
    String menuVendingOptionValue;
    @Value("${component.menu.option-value-water}")
    String menuWaterOptionValue;

    @Value("${component.modal.repair-items-t}")
    String firstTextInputId;
    @Value("${component.modal.repair-reason-t}")
    String secondTextInputId;
    @Value("${component.modal.repair-time-t}")
    String thirdTextInputId;
    final
    RepairMessageBuilder repairMessageBuilder;

    public RepairRequestEvent(RepairModals repairModals, LineNotify lineNotify, LivingRecordService livingRecordService, DiscordLinkService discordLinkService, RepairMessageBuilder repairMessageBuilder) {
        this.repairModals = repairModals;
        this.lineNotify = lineNotify;
        this.livingRecordService = livingRecordService;
        this.discordLinkService = discordLinkService;
        this.repairMessageBuilder = repairMessageBuilder;
    }


    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!repairModals.getModalIdList().contains(event.getModalId())) return;

        var first = event.getValue(firstTextInputId);
        var second = event.getValue(secondTextInputId);
        var third = event.getValue(thirdTextInputId);
        var discordLinkOptional = discordLinkService.findByDiscordId(event.getUser().getId());
        if (discordLinkOptional.isEmpty()) {
            event.reply("無綁定不可進行此操作").setEphemeral(true).queue();
            return;
        }
        var livingRecordOptional = livingRecordService.findByStudentId(discordLinkOptional.get().getStudent().getStudentId());
        if (livingRecordOptional.isEmpty()) {
            event.reply("查無住宿生資料不可操作").setEphemeral(true).queue();
            return;
        }

        LivingRecord livingRecord = livingRecordOptional.get();

        RepairMessageModel model = new RepairMessageModel(
                livingRecord.getStudent().getName(),
                livingRecord.getStudent().getStudentId(),
                livingRecord.getBed().getBedId(),
                livingRecord.getStudent().getPhoneNumber(),
                first == null ? "" : first.getAsString(),
                second == null ? "" : second.getAsString(),
                third == null ? "" : third.getAsString()
        );
        try {
            RepairTokenSet.RepairType type = repairModals.getTypeByModalId(event.getModalId());
            switch (type) {
                case NORMAL -> lineNotify.sendMessage(
                        repairMessageBuilder.normal(model), type
                        );
                case WASH_AND_DRY_MACHINE -> lineNotify.sendMessage(
                        repairMessageBuilder.washAndDryMachine(model), type
                );
                case VENDING -> lineNotify.sendMessage(
                        repairMessageBuilder.vending(model), type
                );
                case WATER_DISPENSER -> lineNotify.sendMessage(
                        repairMessageBuilder.waterDispenser(model), type
                );
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        event.reply("報修成功: " + repairMessageBuilder.normal(model)).setEphemeral(true).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals(repairButtonId)) return;

       var menu = StringSelectMenu.create(repairMenuId)
               .addOption("水電土木", menuNormalOptionValue, "一般維修，包含冷氣、紗窗、門...", Emoji.fromUnicode("U+1F6E0"))
               .addOption("洗、烘衣機", menuWashAndDyuOptionValue, "各樓層洗、烘衣機", Emoji.fromUnicode("U+1F504"))
               .addOption("販賣機", menuVendingOptionValue, "各樓層販賣機", Emoji.fromUnicode("U+1F964"))
               .addOption("飲水機", menuWaterOptionValue, "各樓層飲水機", Emoji.fromUnicode("U+1F4A7"))
               .setMaxValues(1)
               .setPlaceholder("請選擇報修類別")
               .build();

       event.replyComponents(
               ActionRow.of(menu)
       ).setEphemeral(true).queue();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String selectMenuId = event.getMessage().getId();
        StringSelectMenu menu = event.getSelectMenu();
        event.getHook().editMessageComponentsById(selectMenuId).setComponents(
                ActionRow.of(
                        menu.withDisabled(true)
                )
        ).queue();

        String selected = event.getSelectedOptions().get(0).getValue();

        if (selected.equalsIgnoreCase(menuNormalOptionValue)) {
            event.replyModal(repairModals.getNormalModal()).queue();
            return;
        }

        if (selected.equalsIgnoreCase(menuWashAndDyuOptionValue)) {
            event.replyModal(repairModals.getWashAndDryModal()).queue();
            return;
        }

        if (selected.equalsIgnoreCase(menuVendingOptionValue)) {
            event.replyModal(repairModals.getVendingModal()).queue();
            return;
        }

        if (selected.equalsIgnoreCase(menuWaterOptionValue)) {
            event.replyModal(repairModals.getWaterModal()).queue();
            return;
        }

        // TODO: HANDLE
        event.reply("未知錯誤").queue();
    }
}
