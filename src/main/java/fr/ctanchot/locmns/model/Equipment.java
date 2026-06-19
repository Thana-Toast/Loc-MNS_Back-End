package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<LoanRequest> loanRequests = new ArrayList<>();

    @OneToMany(mappedBy = "equipment")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "equipment")
    private List<Document> documents = new ArrayList<>();

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvailabilityHistory> availabilityHistories = new ArrayList<>();

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WearHistory> wearHistories = new ArrayList<>();

}
