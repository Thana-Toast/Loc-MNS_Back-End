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

public class AvailabilityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_availability_history")
    private Long id;

    @Column(name = "start_date", nullable = false)
    @NotNull
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private AvailabilityStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipment", nullable = false)
    @NotNull
    private Equipment equipment;

}
