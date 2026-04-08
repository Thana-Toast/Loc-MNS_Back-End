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

public class WearHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wear_history")
    private Integer id;

    @Column(name = "status_change_date", nullable = false)
    @NotNull
    private LocalDateTime statusChangeDate;

    @Column(name = "status", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private WearStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipment", nullable = false)
    @NotNull
    private Equipment equipment;

}
