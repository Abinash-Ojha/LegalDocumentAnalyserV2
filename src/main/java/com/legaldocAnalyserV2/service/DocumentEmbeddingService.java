package com.legaldocAnalyserV2.service;

import com.legaldocAnalyserV2.model.DocumentChunk;
import com.legaldocAnalyserV2.repository.DocumentChunkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentEmbeddingService {

    private final DocumentChunkRepository chunkRepo;
    private final EmbeddingService embeddingService;

    public DocumentEmbeddingService(
            DocumentChunkRepository chunkRepo,
            EmbeddingService embeddingService
    ) {
        this.chunkRepo = chunkRepo;
        this.embeddingService = embeddingService;
    }

    public void embedDocument(Long documentId) {

        List<DocumentChunk> chunks =
                chunkRepo.findAllUnembeddedByDocumentId(documentId);


        for (DocumentChunk chunk : chunks) {

            String vector;
            try {
                vector = embeddingService.embedAsPgVector(chunk.getContent());
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to embed chunk id=" + chunk.getId()
                                + " documentId=" + documentId,
                        e
                );
            }


            updateEmbeddingTransactional(chunk.getId(), vector);
        }

    }

    @Transactional
    protected void updateEmbeddingTransactional(Long chunkId, String vector) {
        chunkRepo.updateEmbedding(chunkId, vector);
    }
}
