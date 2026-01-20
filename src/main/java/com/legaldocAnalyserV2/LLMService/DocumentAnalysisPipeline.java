package com.legaldocAnalyserV2.LLMService;
import com.legaldocAnalyserV2.prompt.LegalPromptBuilder;
import com.legaldocAnalyserV2.service.PdfExtractionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentAnalysisPipeline {

    private final PdfExtractionService pdfService;
    private final GeminiClient geminiClient;

    public DocumentAnalysisPipeline(PdfExtractionService pdfService,
                                    GeminiClient geminiClient) {
        this.pdfService = pdfService;
        this.geminiClient = geminiClient;
    }

    public String analyze(MultipartFile file) {

        // 1. Extract text
        String text = pdfService.extractText(file);

        // 2. Build prompt
        String prompt = LegalPromptBuilder.buildAnalysisPrompt(text);

        // 3. Send to Gemini
        return geminiClient.analyze(prompt);
    }
}

