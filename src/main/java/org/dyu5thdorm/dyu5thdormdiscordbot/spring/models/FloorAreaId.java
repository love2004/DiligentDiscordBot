package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class FloorAreaId implements Serializable {
    private Long floor;
    private String areaId;
}
