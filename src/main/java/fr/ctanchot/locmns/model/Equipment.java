package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipment")
    private Long id;

    @Column(name = "serial_number", length = 50, nullable = false)
    @NotBlank
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_model", nullable = false)
    @NotNull
    private Model model;

    @OneToMany(mappedBy = "equipment")
    private Set<LoanRequest> loanRequests = new HashSet<>();

    @OneToMany(mappedBy = "equipment")
    private Set<Ticket> tickets = new HashSet<>();

}
