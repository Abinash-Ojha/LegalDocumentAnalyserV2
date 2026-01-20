package com.legaldocAnalyserV2.LLMService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legaldocAnalyserV2.DTO.GeminiEmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class GeminiEmbeddingClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.embedding.model-path}")
    private String embeddingModelPath;

    public GeminiEmbeddingClient(
            WebClient.Builder builder,
            @Value("${gemini.api.base-url}") String baseUrl,
            ObjectMapper objectMapper,
            @Value("${gemini.embedding.api-key}") String apiKey
    ) {
        this.objectMapper = objectMapper;
        this.webClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("x-goog-api-key", apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public float[] embed(String text) {

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text is empty for embedding");
        }

        Map<String, Object> body = Map.of(
                "content", Map.of(
                        "parts", List.of(
                                Map.of("text", text)
                        )
                )
        );

        return webClient.post()
                .uri(embeddingModelPath) // MUST be embedContent endpoint
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(raw ->
                        System.err.println("RAW GEMINI EMBEDDING JSON = " + raw)
                )
                .map(raw -> {
                    try {
                        GeminiEmbeddingResponse resp =
                                objectMapper.readValue(raw, GeminiEmbeddingResponse.class);
                        return resp.toFloatArray();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse Gemini embedding JSON", e);
                    }
                })
                .block();
    }

}
