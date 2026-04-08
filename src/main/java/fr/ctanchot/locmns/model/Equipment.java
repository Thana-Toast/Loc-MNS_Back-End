package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipment")
    private Integer id;

    @Column(name = "serial_number", length = 50, nullable = false)
    @NotBlank
    private String serialNumber;

    @Column(name = "label", length = 100, nullable = false)
    @NotBlank
    private String label;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String description;

}
