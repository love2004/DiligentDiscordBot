package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "cadre")
@Data
@NoArgsConstructor
@Component
public class Cadre {
    @OneToOne
    @Id
    @JoinColumn(name = "cadre_id", referencedColumnName = "student_id")
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
