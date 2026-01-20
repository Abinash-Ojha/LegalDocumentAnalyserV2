package com.legaldocAnalyserV2.LLMService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
@Component
public class GeminiChatClient {


    private final WebClient webClient;

    @Value("${gemini.api.model-path}")
    private String modelPath;

    @Value("${gemini.chat.api-key}")
    private String apiKey;

    public GeminiChatClient(
            WebClient.Builder builder,
            @Value("${gemini.api.base-url}") String baseUrl
    ) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public String chat(String prompt) {
        System.err.println(">>> ENTERED GeminiChatClient.chat()");
        System.err.println(">>> Prompt length = " + prompt.length());


        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                },
                "generationConfig", Map.of(
                        "temperature", 0.2,
                        "maxOutputTokens", 400
                )
        );


        return webClient.post()
                .uri(modelPath)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(err -> new RuntimeException("Gemini error body: " + err))
                )
                .bodyToMono(String.class)
                .block();
    }



    }
