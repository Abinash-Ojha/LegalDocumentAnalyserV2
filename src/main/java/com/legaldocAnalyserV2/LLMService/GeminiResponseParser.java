package com.legaldocAnalyserV2.LLMService;
import java.util.HashMap;
import java.util.Map;

public class GeminiResponseParser {

    public static Map<String, String> parse(String response) {

        Map<String, String> result = new HashMap<>();

        result.put("SUMMARY", extract(response, "SUMMARY:", "KEY_CLAUSES:"));
        result.put("KEY_CLAUSES", extract(response, "KEY_CLAUSES:", "PARTY_OBLIGATIONS:"));
        result.put("PARTY_OBLIGATIONS", extract(response, "PARTY_OBLIGATIONS:", "RISK_FACTORS:"));
        result.put("RISK_FACTORS", extract(response, "RISK_FACTORS:", "IMPROVEMENTS:"));
        result.put("IMPROVEMENTS", extract(response, "IMPROVEMENTS:", null));

        return result;
    }

    private static String extract(String text, String start, String end) {
        int startIdx = text.indexOf(start);
        if (startIdx == -1) return "";

        startIdx += start.length();

        int endIdx = (end == null) ? text.length() : text.indexOf(end);
        if (endIdx == -1) endIdx = text.length();

        return text.substring(startIdx, endIdx).trim();
    }
}

