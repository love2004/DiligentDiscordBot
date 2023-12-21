package org.dyu5thdorm.dyu5thdormdiscordbot.spring.view;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity()
@Table(name = "ticket_view")
public class TicketView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "atd_rate")
    private Double attendanceRate;

    @Column(name = "ticket_count")
    private Double ticketCount;
}
