package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class Role {
    @Id
    @Column(name = "role_id")
    private String id;

    @Column(name = "name")
    private String name;
}
