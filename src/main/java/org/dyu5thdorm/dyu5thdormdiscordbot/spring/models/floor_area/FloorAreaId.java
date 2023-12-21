package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area;

import lombok.Data;

import java.io.Serializable;

@Data
public class FloorAreaId implements Serializable {
    private Integer floor;
    private String areaId;
}
