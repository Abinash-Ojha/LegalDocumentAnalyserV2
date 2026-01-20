package com.legaldocAnalyserV2.LLMService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeminiResponseExtractor {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String extractText(String rawJson) {
        try {
            JsonNode root = mapper.readTree(rawJson);

            return root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Gemini text", e);
        }
    }
}

