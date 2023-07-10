package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.LivingRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.LivingRecordService;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@PropertySource("classpath:discord.properties")
public class SearchByStudentId extends ListenerAdapter {
    @Value("${component.button.student-info-by-studentId}")
    String studentIdButtonId;
    @Value("${component.modal.student-info-by-studentId}")
    String modelId;
    @Value("${component.modal.student-info-by-studentId-t}")
    String textInputId;
    @Autowired
    LivingRecordService livingRecordService;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals(studentIdButtonId)) return;

        event.replyModal(
                Modal.create(modelId, "以學號查詢住宿生")
                        .addActionRow(
                                TextInput.create(textInputId, "學號", TextInputStyle.SHORT)
                                        .setMinLength(8)
                                        .setMaxLength(8)
                                        .setRequired(true)
                                        .setPlaceholder("F0000000")
                                        .build()
                        )
                        .build()
        ).queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals(modelId)) return;

        String searchStudentId = event.getValue(textInputId).getAsString();

        //TODO
    }
}
