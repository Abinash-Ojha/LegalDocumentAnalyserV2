package com.legaldocAnalyserV2.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiEmbeddingResponse {

    private Embedding embedding;

    public Embedding getEmbedding() {
        return embedding;
    }

    public void setEmbedding(Embedding embedding) {
        this.embedding = embedding;
    }

    public float[] toFloatArray() {

        if (embedding == null || embedding.values == null || embedding.values.isEmpty()) {
            throw new IllegalStateException("Gemini returned empty embeddings");
        }

        float[] vector = new float[embedding.values.size()];
        for (int i = 0; i < embedding.values.size(); i++) {
            vector[i] = embedding.values.get(i);
        }
        return vector;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedding {
        public List<Float> values;
    }
}
