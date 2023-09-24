package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "floor_area")
@IdClass(FloorAreaId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class FloorArea {
    @Id
    @Column(name = "floor")
    private Long floor;

    @Id
    @Column(name = "area_id")
    private String areaId;
}
