package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "living_record")
@IdClass(LivingRecordId.class)
@Data
@NoArgsConstructor
public class LivingRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "bed_id", referencedColumnName = "bed_id")
    private Bed bed;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "school_year", referencedColumnName = "school_year"),
            @JoinColumn(name = "semester", referencedColumnName = "semester")
    })
    private SchoolTimestamp schoolTimestamp;

    @Column(name = "update_time")
    private Date updateTime;
}

