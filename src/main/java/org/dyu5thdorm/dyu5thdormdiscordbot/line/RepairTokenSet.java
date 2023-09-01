package org.dyu5thdorm.dyu5thdormdiscordbot.line;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:line.properties")
public class RepairTokenSet {
    @Value("${token.normal}")
    private String normal;
    @Value("${token.wash-and-dry}")
    private String washAndDry;
    @Value("${token.vending}")
    private String vending;
    @Value("${token.water}")
    private String water;

    public enum RepairType {NORMAL, WASH_AND_DRY_MACHINE, VENDING, WATER_DISPENSER}

    public String getTokenByType(RepairType type) {
        switch (type) {
            case NORMAL -> {
                return this.normal;
            }
            case WASH_AND_DRY_MACHINE -> {
                return this.washAndDry;
            }
            case VENDING -> {
                return this.vending;
            }
            case WATER_DISPENSER -> {
                return this.water;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
