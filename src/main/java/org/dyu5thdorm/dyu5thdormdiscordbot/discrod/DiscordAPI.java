package org.dyu5thdorm.dyu5thdormdiscordbot.discrod;

import jakarta.annotation.PostConstruct;
import lombok.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class DiscordAPI {
    List<ListenerAdapter> listenerAdapters;
    JDA jda;
    Guild guild;
    @Value("${token}")
    String token;

    @Autowired
    public DiscordAPI(List<ListenerAdapter> listenerAdapters) {
        this.listenerAdapters = listenerAdapters;
    }

    @PostConstruct
    public void init() {
        buildJDA();
    }

    @SneakyThrows
    void buildJDA() {
        JDABuilder builder = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL);
        listenerAdapters.forEach(builder::addEventListeners);
        jda = builder.build();
        jda.awaitReady();
    }
}
