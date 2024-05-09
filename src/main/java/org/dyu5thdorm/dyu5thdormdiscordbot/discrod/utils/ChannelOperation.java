package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChannelOperation {
    @Getter
    final
    ChannelIdSet channelIdSet;
    Map<Integer, String > floorChannelMap;

    @PostConstruct
    void init() {
        floorChannelMap = Map.of(
                1, channelIdSet.getFloorOne(),
                2, channelIdSet.getFloorTwo(),
                3, channelIdSet.getFloorThree(),
                4, channelIdSet.getFloorFour(),
                5, channelIdSet.getFloorFive(),
                6, channelIdSet.getFloorSix()
        );
    }

    public void deleteAllMessage(@NotNull TextChannel textChannel, int limit) {
        textChannel.getHistoryFromBeginning(limit).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );
    }
}
