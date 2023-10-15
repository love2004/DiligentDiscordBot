package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area.FloorArea;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "took_coin")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TookCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "floor", referencedColumnName = "floor"),
            @JoinColumn(name = "area_id", referencedColumnName = "area_id")
    })
    private FloorArea floor;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @Column(name = "machine")
    private String machine;

    @Column(name = "description")
    private String description;

    @Column(name = "coin_amount")
    private Integer coinAmount;

    @CreationTimestamp
    @Column(name = "record_time")
    private LocalDateTime recordTime;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "get_back_time")
    private LocalDateTime getBackTime;
}
