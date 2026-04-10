package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long id;

    @Column(name = "label", length = 100, nullable = false)
    @NotBlank
    private String label;

    @Column(name = "description", length = 200, nullable = false)
    @NotBlank
    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Equipment> equipments = new HashSet<>();

}
