package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "floor_area")
@IdClass(FloorAreaId.class)
@Data
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
