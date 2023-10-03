package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Cadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;

@Entity
@Table(name = "floor_area_cadre")
@IdClass(FloorAreaCadreId.class)
@Getter
@Setter
@ToString
public class FloorAreaCadre {
    @Id
    @OneToOne
    @JoinColumn(name = "cadre_id", referencedColumnName = "cadre_id", insertable = false, updatable = false)
    private Cadre cadre;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "floor", referencedColumnName = "floor", insertable = false, updatable = false),
            @JoinColumn(name = "area_id", referencedColumnName = "area_id", insertable = false, updatable = false)
    })
    private FloorArea floorArea;
}
