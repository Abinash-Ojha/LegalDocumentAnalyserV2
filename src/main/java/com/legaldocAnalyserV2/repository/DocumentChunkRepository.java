package com.legaldocAnalyserV2.repository;
import com.legaldocAnalyserV2.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentChunkRepository
        extends JpaRepository<DocumentChunk, Long> {

    @Query(value = """
       SELECT content
                        FROM document_chunks
                        WHERE document_id = :documentId
                          AND embedding IS NOT NULL
                        ORDER BY embedding <=> CAST(:queryEmbedding AS vector)
                        LIMIT :k
    """, nativeQuery = true)
    List<String> findTopKByDocument(
            @Param("documentId") Long documentId,
            @Param("queryEmbedding") String queryEmbedding,
            @Param("k") int k
    );
    @Modifying
    @Query(value = """
    UPDATE document_chunks
    SET embedding = CAST(:embedding AS vector)
    WHERE id = :chunkId
""", nativeQuery = true)
    void updateEmbedding(
            @Param("chunkId") Long chunkId,
            @Param("embedding") String embedding
    );
    List<DocumentChunk> findAllByDocumentId(Long documentId);

    @Query("""
    SELECT c FROM DocumentChunk c
    WHERE c.document.id = :documentId
      AND c.embedding IS NULL
""")
    List<DocumentChunk> findAllUnembeddedByDocumentId(@Param("documentId") Long documentId);



}
