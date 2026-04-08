package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket_history")
    private Integer id;

    @Column(name = "state_change_date", nullable = false)
    @NotNull
    private LocalDateTime stateChangeDate;

    @Column(name = "status", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket", nullable = false)
    @NotNull
    private Ticket ticket;

}
