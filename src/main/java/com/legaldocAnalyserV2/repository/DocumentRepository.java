package com.legaldocAnalyserV2.repository;

import com.legaldocAnalyserV2.model.Document;
import com.legaldocAnalyserV2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByUserAndId(
            User user,
            Long id
    );

    Optional<Document> findByUserAndOriginalFilename(
            User user,
            String originalFilename
    );
    List<Document> findAllByUser(User user);
}
