package org.dyu5thdorm.dyu5thdormdiscordbot.line;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@PropertySource("classpath:line.properties")
@Getter
public class RepairTokenSet {
    private final Map<RepairType, String> tokenMap;

    public enum RepairType { NORMAL, WASH_AND_DRY_MACHINE, VENDING, WATER_DISPENSER }

    public RepairTokenSet(@Value("${token.normal}") String normal,
                          @Value("${token.wash-and-dry}") String washAndDry,
                          @Value("${token.vending}") String vending,
                          @Value("${token.water}") String water) {
        tokenMap = new EnumMap<>(RepairType.class);
        tokenMap.put(RepairType.NORMAL, normal);
        tokenMap.put(RepairType.WASH_AND_DRY_MACHINE, washAndDry);
        tokenMap.put(RepairType.VENDING, vending);
        tokenMap.put(RepairType.WATER_DISPENSER, water);
    }
}
