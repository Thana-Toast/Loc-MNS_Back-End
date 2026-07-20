package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentDao extends JpaRepository<Document, Long> {

    @Query("SELECT d.filePath FROM Document d WHERE d.filePath IS NOT NULL")
    List<String> findAllFilePaths();
}
