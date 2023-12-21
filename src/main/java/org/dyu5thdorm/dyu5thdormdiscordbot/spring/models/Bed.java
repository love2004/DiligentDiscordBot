package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bed")
@Getter
@Setter
@ToString
public class Bed {
    @Id
    @Column(name = "bed_id")
    String bedId;
}
