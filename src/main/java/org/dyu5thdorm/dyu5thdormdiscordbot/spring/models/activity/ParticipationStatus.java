package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity()
@Builder
@Table(name = "participation_status_definitions")
@AllArgsConstructor
public class ParticipationStatus {
    @Id
    @Column(name = "status_code")
    Integer statusCode;

    @Column(name = "status_description")
    String description;
}
