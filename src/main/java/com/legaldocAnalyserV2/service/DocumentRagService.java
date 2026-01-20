package com.legaldocAnalyserV2.service;
import com.legaldocAnalyserV2.LLMService.GeminiChatClient;
import com.legaldocAnalyserV2.LLMService.GeminiClient;
import com.legaldocAnalyserV2.LLMService.GeminiResponseExtractor;
import com.legaldocAnalyserV2.repository.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentRagService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository chunkRepo;
    private final GeminiClient geminiClient;
    private final GeminiChatClient geminiChatClient;

    public DocumentRagService(
            EmbeddingService embeddingService,
            DocumentChunkRepository chunkRepo,
            GeminiClient geminiClient,
            GeminiChatClient geminiChatClient
    ) {
        this.embeddingService = embeddingService;
        this.chunkRepo = chunkRepo;
        this.geminiClient = geminiClient;
        this.geminiChatClient=geminiChatClient;
    }

    public String ask(Long documentId, String question) {
        System.out.println("Inside Rag Service--->> going to the GeminiChatClient");


        System.out.println("Using GeminiChatClient = " + geminiChatClient.getClass());


        String queryVector = embeddingService.embedAsPgVector(question);

        List<String> chunks =
                chunkRepo.findTopKByDocument(documentId, queryVector, 5);
            System.out.println(chunks);

        if (chunks.isEmpty()) {
            return "Answer not found in document.";
        }

        String context = chunks.stream()
                .map(c -> c.length() > 800 ? c.substring(0, 800) : c)
                .reduce("", (a, b) -> a + "\n---\n" + b);

        String prompt = """
You are a legal document analysis assistant.
Use ONLY the context below to answer the query of the user, provide whatever matches with the query.
If the answer is not present, say: "Not found in document".

Context:
%s

Question:
%s

Answer:
""".formatted(context, question);

        String rawJson = geminiChatClient.chat(prompt);
        return GeminiResponseExtractor.extractText(rawJson);
    }
}
