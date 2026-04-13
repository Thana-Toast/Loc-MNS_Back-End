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

public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_model")
    private Long id;

    @Column(name = "label", length = 100, nullable = false)
    @NotBlank
    private String label;

    @Column(name = "brand", length = 50, nullable = false)
    @NotBlank
    private String brand;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String description;

    @Column(name = "technical_specs", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String technicalSpecs;

    @Column(name = "image_path", length = 255")
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false)
    @NotNull
    private Category category;

    @OneToMany(mappedBy = "model")
    private Set<Equipment> equipments = new HashSet<>();

}
