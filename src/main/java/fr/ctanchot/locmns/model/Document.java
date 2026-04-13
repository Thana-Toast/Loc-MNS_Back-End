package fr.ctanchot.locmns.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    @NotBlank
    private String title;

    @Column(name = "file_path", length = 255, nullable = false)
    @NotBlank
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doc_type", nullable = false)
    @NotNull
    private DocType docType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_model")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipment")
    private Equipment equipment;

    @AssertTrue(message = "Un document doit être associé SOIT à un modèle SOIT à un équipement")
    public boolean isValidXorRelationship() {

        boolean hasModel = (this.model != null);
        boolean hasEquipment = (this.equipment != null);

        return (hasModel && !hasEquipment) || (!hasModel && hasEquipment);
    }

}
