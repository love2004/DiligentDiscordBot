package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "no_call_roll_date")
@Getter
@Setter
@ToString
public class NoCallRollDate {
    @Id
    @Column(name = "day")
    LocalDate day;
}
