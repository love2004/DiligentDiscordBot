package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.utils;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
        floorChannelMap = new HashMap<>();
        floorChannelMap.put(1, channelIdSet.getFloorOne());
        floorChannelMap.put(2, channelIdSet.getFloorTwo());
        floorChannelMap.put(3, channelIdSet.getFloorThree());
        floorChannelMap.put(4, channelIdSet.getFloorFour());
        floorChannelMap.put(5, channelIdSet.getFloorFive());
        floorChannelMap.put(6, channelIdSet.getFloorThree());
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
