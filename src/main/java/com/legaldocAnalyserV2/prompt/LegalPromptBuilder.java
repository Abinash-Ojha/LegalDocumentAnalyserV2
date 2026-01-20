package com.legaldocAnalyserV2.prompt;
public class LegalPromptBuilder {

    public static String buildAnalysisPrompt(String documentText) {

        return """
        You are a legal expert.

        Analyze the following legal document and respond STRICTLY
        in the format below. Do not add extra text.

        SUMMARY:
        <text>

        KEY_CLAUSES:
        <text>

        PARTY_OBLIGATIONS:
        <text>

        RISK_FACTORS:
        <text>

        IMPROVEMENTS:
        <text>

        DOCUMENT:
        %s
        """.formatted(documentText);
    }
}

