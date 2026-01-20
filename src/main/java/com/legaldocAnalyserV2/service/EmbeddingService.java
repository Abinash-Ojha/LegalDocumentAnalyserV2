package com.legaldocAnalyserV2.service;

import com.legaldocAnalyserV2.LLMService.GeminiEmbeddingClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmbeddingService {

    private final GeminiEmbeddingClient embeddingClient;

    public EmbeddingService(GeminiEmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    /**
     * Returns pgvector-compatible string:
     * [0.123,0.456,...]
     */
    public String embedAsPgVector(String text) {

        if (text == null || text.trim().length() < 3) {
            throw new IllegalArgumentException("Text too short for embedding");
        }


        float[] vector = embeddingClient.embed(text);

        if (vector == null || vector.length == 0) {
            throw new IllegalStateException("Gemini returned empty embedding");
        }

        // pgvector expects: [0.1,0.2,0.3]
        return Arrays.toString(vector).replace(" ", "");
    }
}
