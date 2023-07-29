package org.dyu5thdorm.dyu5thdormdiscordbot.discrod;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development.DevelopmentOperationEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development.GenerateRequest;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development.GenerateRules;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development.ShutdownButton;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByDiscord;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByName;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByRoom;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByStudentId;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth.OnAuthButtonInteractionEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth.OnAuthModalInteractionEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth.OnAuthedUserLeaveEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair.RepairRequestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:discord.properties")
@Data
@NoArgsConstructor
public class DiscordAPI {
    @Value("${token}")
    String token;
    JDA jda;
    Guild guild;
    @Autowired
    DevelopmentOperationEvent developmentOperationEvent;
    @Autowired
    GenerateRules generateRules;
    @Autowired
    GenerateRequest generateRequestButton;
    @Autowired
    ShutdownButton shutdownButton;
    @Autowired
    OnAuthButtonInteractionEvent onAuthButtonInteraction;
    @Autowired
    OnAuthModalInteractionEvent onAuthModalInteraction;
    @Autowired
    OnAuthedUserLeaveEvent onAuthedUserLeaveEvent;
    @Autowired
    SearchByDiscord searchByDiscord;
    @Autowired
    SearchByRoom searchByRoom;
    @Autowired
    SearchByStudentId searchByStudentId;
    @Autowired
    SearchByName searchByName;
    @Autowired
    RepairRequestEvent repairRequestEvent;

    @PostConstruct
    void init() {
        buildJDA();
    }

    void buildJDA() {
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(
                        developmentOperationEvent,
                        generateRules,
                        generateRequestButton,
                        shutdownButton,
                        onAuthModalInteraction,
                        onAuthButtonInteraction,
                        onAuthedUserLeaveEvent,
                        searchByDiscord,
                        searchByRoom,
                        searchByStudentId,
                        searchByName,
                        repairRequestEvent
                )
                .build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
