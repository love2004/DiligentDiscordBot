package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@Component
public class Role {
    @Id
    @Column(name = "role_id")
    private String id;

    @Column(name = "name")
    private String name;
}
