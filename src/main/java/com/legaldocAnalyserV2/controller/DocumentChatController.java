package com.legaldocAnalyserV2.controller;
import com.legaldocAnalyserV2.service.DocumentRagService;
import com.legaldocAnalyserV2.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/documents")
public class DocumentChatController {

    private final DocumentRagService ragService;
    private final DocumentService documentService;

    public DocumentChatController(
            DocumentRagService ragService,
            DocumentService documentService
    ) {
        this.ragService = ragService;
        this.documentService = documentService;
    }

    @PostMapping("/{documentId}/ask")
    public ResponseEntity<?> askDocument(
            @PathVariable Long documentId,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {

        System.out.print("Inside the Chat Controller going to the ragService->> ");


        documentService.verifyOwnership(documentId, authentication);

        // 2️⃣ Extract question
        String question = body.get("question");

        System.out.println("Controller question is"+question);

        // 3️⃣ RAG answer
        String answer = ragService.ask(documentId, question);

        return ResponseEntity.ok(Map.of(
                "documentId", documentId,
                "question", question,
                "answer", answer
        ));
    }
}

