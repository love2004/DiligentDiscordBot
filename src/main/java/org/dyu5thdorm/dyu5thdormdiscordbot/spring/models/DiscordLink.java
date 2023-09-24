package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Table(name = "discord_link")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class DiscordLink {
    @Id
    @Column(name = "discord_id")
    private String discordId;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @CreationTimestamp
    @Column(name = "linked_time")
    private LocalDateTime linkedTime;

    public DiscordLink(String discordId) {
        this.discordId = discordId;
    }
}
