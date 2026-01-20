package com.legaldocAnalyserV2.LLMService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class GeminiClient {
    private final WebClient webClient;
    @Value("${gemini.api.model-path}")
    private String modelPath;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiClient(
            WebClient.Builder builder,
            @Value("${gemini.api.base-url}") String baseUrl
    ) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public String analyze(String prompt) {


        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        return webClient.post()
                .uri(modelPath)
                .header("x-goog-api-key", apiKey) // 🔑 THIS IS THE KEY DIFFERENCE
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
