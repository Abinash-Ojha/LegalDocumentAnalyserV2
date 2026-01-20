package com.legaldocAnalyserV2.service;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkingService {

    private static final int CHUNK_SIZE = 1000;
    private static final int OVERLAP = 150;

    public List<String> chunk(String text) {
        List<String> chunks = new ArrayList<>();

        int length = text.length();
        int start = 0;

        while (start < length) {
            int end = Math.min(start + CHUNK_SIZE, length);
            chunks.add(text.substring(start, end));

            if (end == length) {
                break; // 🔥 THIS LINE FIXES OOM
            }

            start = end - OVERLAP;
            if (start < 0) start = 0;
        }

        return chunks;
    }

}

