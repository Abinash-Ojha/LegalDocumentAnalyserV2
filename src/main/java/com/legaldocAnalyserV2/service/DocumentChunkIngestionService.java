package com.legaldocAnalyserV2.service;

import com.legaldocAnalyserV2.model.Document;
import com.legaldocAnalyserV2.model.DocumentChunk;
import com.legaldocAnalyserV2.repository.DocumentChunkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentChunkIngestionService {

    private final TextChunkingService chunkingService;
    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository chunkRepo;

    public DocumentChunkIngestionService(
            TextChunkingService chunkingService,
            EmbeddingService embeddingService,
            DocumentChunkRepository chunkRepo
    ) {
        this.chunkingService = chunkingService;
        this.embeddingService = embeddingService;
        this.chunkRepo = chunkRepo;
    }

    @Transactional
    public void ingest(Document document, String text) {

        List<String> chunks = chunkingService.chunk(text);

        int index = 0;
        for (String chunkText : chunks) {
            DocumentChunk chunk = new DocumentChunk();
            chunk.setDocument(document);
            chunk.setChunkIndex(index++);
            chunk.setContent(chunkText);
            chunkRepo.save(chunk);
        }
    }

}
