package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Cadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;

import java.io.Serializable;

@Data
public class FloorAreaCadreId implements Serializable {
    private Cadre cadre;
    private FloorArea floorArea;
}
