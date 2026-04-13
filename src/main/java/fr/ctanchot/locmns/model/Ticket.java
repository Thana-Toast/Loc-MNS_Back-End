package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long id;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDateTime date;

    @Column(name = "label", length = 50, nullable = false)
    @NotBlank
    private String label;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipment", nullable = false)
    @NotNull
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    @NotNull
    private AppUser appUser;

}
