package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChannelOperation {
    @Getter
    final
    ChannelIdSet channelIdSet;
    Map<Integer, String > floorChannelMap;

    public ChannelOperation(ChannelIdSet channelIdSet) {
        this.channelIdSet = channelIdSet;
    }

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

    public void deleteAllMessage(TextChannel textChannel, int limit) {
        if (textChannel == null) {
            return;
        }

        textChannel.getHistoryFromBeginning(limit).complete().getRetrievedHistory().forEach(
                message -> message.delete().queue()
        );
    }

    public String  getFloorChannelByFloor(Integer floor) {
        return floorChannelMap.getOrDefault(floor, null);
    }
}
