package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class FloorAreaId implements Serializable {
    private Integer floor;
    private String areaId;
}
