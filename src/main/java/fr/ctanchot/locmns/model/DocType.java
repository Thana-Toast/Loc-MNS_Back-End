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

public class DocType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doc_type")
    private Long id;

    @Column(name = "label", length = 30, nullable = false)
    @NotBlank
    private String label;

    @OneToMany(mappedBy = "docType")
    private Set<Document> documents = new HashSet<>();

}
