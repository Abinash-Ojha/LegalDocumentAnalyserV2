package com.legaldocAnalyserV2.DTO;

import java.util.List;

public class GeminiChatResponse {

    private List<Candidate> candidates;

    public String extractText() {
        if (candidates == null || candidates.isEmpty()) {
            return "";
        }

        Candidate first = candidates.get(0);
        if (first.content == null || first.content.parts == null || first.content.parts.isEmpty()) {
            return "";
        }

        return first.content.parts.get(0).text;
    }

    static class Candidate {
        private Content content;
    }

    static class Content {
        private List<Part> parts;
    }

    static class Part {
        private String text;
    }
}

