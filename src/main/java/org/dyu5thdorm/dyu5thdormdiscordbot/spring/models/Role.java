package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
public class Role {
    @Id
    @Column(name = "role_id")
    private String id;

    @Column(name = "name")
    private String name;
}
