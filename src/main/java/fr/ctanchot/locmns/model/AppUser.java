package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_appuser")
    private Long id;

    @Column(name = "last_name", length = 50, nullable = false)
    @NotBlank
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    @NotBlank
    private String firstName;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    @NotBlank
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    @NotBlank
    private String password;

    @Column(name = "creation_date", nullable = false)
    @NotNull
    private LocalDateTime creationDate;

    @Column(name = "consent_date")
    private LocalDateTime consentDate;

    @Column(name = "departure_date")
    private LocalDateTime departureDate;

    @Column(name = "is_anonymized")
    private Boolean isAnonymized;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "id_appuser"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "appUser")
    private List<LoanRequest> loanRequests = new ArrayList<>();

    @OneToMany(mappedBy = "appUser")
    private List<Ticket> tickets = new ArrayList<>();

}
