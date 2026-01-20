package com.legaldocAnalyserV2.service;
import com.legaldocAnalyserV2.LLMService.GeminiClient;
import com.legaldocAnalyserV2.LLMService.GeminiResponseExtractor;
import com.legaldocAnalyserV2.LLMService.GeminiResponseParser;
import com.legaldocAnalyserV2.model.Document;
import com.legaldocAnalyserV2.model.DocumentAnalysis;
import com.legaldocAnalyserV2.model.User;
import com.legaldocAnalyserV2.prompt.LegalPromptBuilder;
import com.legaldocAnalyserV2.repository.DocumentAnalysisRepository;
import com.legaldocAnalyserV2.repository.DocumentRepository;
import com.legaldocAnalyserV2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DocumentService {

    private final PdfExtractionService pdfService;
    private final GeminiClient geminiClient;
    private final DocumentRepository documentRepo;
    private final DocumentAnalysisRepository analysisRepo;
    private final DocumentChunkIngestionService chunkIngestionService;
    private final DocumentEmbeddingService documentEmbeddingService;

    @Autowired
    private UserRepo userRepo;


    public DocumentService(
            PdfExtractionService pdfService,
            GeminiClient geminiClient,
            DocumentRepository documentRepo,
            DocumentAnalysisRepository analysisRepo,
            DocumentChunkIngestionService chunkIngestionService,
            DocumentEmbeddingService documentEmbeddingService

    ) {
        this.pdfService = pdfService;
        this.geminiClient = geminiClient;
        this.documentRepo = documentRepo;
        this.analysisRepo = analysisRepo;
        this.chunkIngestionService=chunkIngestionService;
        this.documentEmbeddingService = documentEmbeddingService;

    }

    public Optional<DocumentAnalysis> findExistingAnalysis(
            User user,
            String filename
    ) {
        return documentRepo
                .findByUserAndOriginalFilename(user, filename)
                .map(Document::getAnalysis);
    }
    public List<Document> findAllByUser(User user) {
        return documentRepo.findAllByUser(user);
    }


    @Transactional
    public DocumentAnalysis analyzeAndSave(MultipartFile file, User user) {

        String filename = file.getOriginalFilename();

        Optional<Document> existing =
                documentRepo.findByUserAndOriginalFilename(user, filename);

        if (existing.isPresent() && existing.get().getAnalysis() != null) {
            return existing.get().getAnalysis();
        }


        Document document = new Document();
        document.setUser(user);
        document.setOriginalFilename(filename);
        documentRepo.save(document);

        String text = pdfService.extractText(file);
        chunkIngestionService.ingest(document, text);
        System.out.println(">>> STARTING VECTOR INDEXING FOR DOCUMENT " + document.getId());
        documentEmbeddingService.embedDocument(document.getId());
        System.out.println(">>> FINISHED VECTOR INDEXING FOR DOCUMENT " + document.getId());




        String prompt = LegalPromptBuilder.buildAnalysisPrompt(text);

        String rawJson = geminiClient.analyze(prompt);
        String modelText = GeminiResponseExtractor.extractText(rawJson);
        Map<String, String> parsed = GeminiResponseParser.parse(modelText);

        DocumentAnalysis analysis = new DocumentAnalysis();
        analysis.setDocument(document);
        analysis.setSummary(parsed.get("SUMMARY"));
        analysis.setKeyClauses(parsed.get("KEY_CLAUSES"));
        analysis.setPartyObligations(parsed.get("PARTY_OBLIGATIONS"));
        analysis.setRiskFactors(parsed.get("RISK_FACTORS"));
        analysis.setImprovements(parsed.get("IMPROVEMENTS"));
        analysis.setModelUsed("gemini-3-flash-preview");
        analysis.setPromptVersion("v1");

        return analysisRepo.save(analysis);
    }
    public void verifyOwnership(
            Long documentId,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = documentRepo
                .findByUserAndId(user, documentId)
                .isPresent();

        if (!exists) {
            throw new RuntimeException("Unauthorized access");
        }
    }

}




