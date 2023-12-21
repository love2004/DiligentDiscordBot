package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;

@Entity
@Table(name = "cadre")
@Getter
@Setter
@ToString
public class Cadre {
    @Id
    @Column(name = "cadre_id")
    private Integer cadreId;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "school_year", referencedColumnName = "school_year"),
            @JoinColumn(name = "semester", referencedColumnName = "semester")
    })
    private SchoolTimestamp schoolTimestamp;
}
