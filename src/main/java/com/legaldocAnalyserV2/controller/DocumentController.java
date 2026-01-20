package com.legaldocAnalyserV2.controller;
import com.legaldocAnalyserV2.DTO.DocumentAnalysisResponse;
import com.legaldocAnalyserV2.DTO.UserDocumentResponse;
import com.legaldocAnalyserV2.model.DocumentAnalysis;
import com.legaldocAnalyserV2.model.User;
import com.legaldocAnalyserV2.repository.UserRepo;
import com.legaldocAnalyserV2.security.RateLimiterService;
import com.legaldocAnalyserV2.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final UserRepo userRepo;
    private final RateLimiterService rateLimiterService;

    public DocumentController(DocumentService documentService,
                              UserRepo userRepo,RateLimiterService rateLimiterService) {
        this.documentService = documentService;
        this.userRepo = userRepo;
        this.rateLimiterService=rateLimiterService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(
            @RequestPart MultipartFile file,
            Authentication authentication,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();

        String userIdStr = (String) authentication.getPrincipal();
        User user = userRepo.findById(Long.parseLong(userIdStr))
                .orElseThrow();

        String filename = file.getOriginalFilename();

        // Check if document already exists
        var existing =
                documentService.findExistingAnalysis(user, filename);

        if (existing.isPresent()) {
            if (!rateLimiterService.allowPerMinute(ip)) {
                return ResponseEntity.status(429).body("Too Many requests");
            }
            return ResponseEntity.ok(mapToResponse(existing.get()));
        }

        //  New document → daily limiter
        if (!rateLimiterService.allowDaily(ip)) {
            return ResponseEntity.status(429).body("You Hit To days Limit, upgrade the plan for more access else wait for  hours ");
        }

        // 3️⃣ Full analysis
        DocumentAnalysis analysis =
                documentService.analyzeAndSave(file, user);

        return ResponseEntity.ok(mapToResponse(analysis));
    }


    private DocumentAnalysisResponse mapToResponse(DocumentAnalysis analysis) {
        DocumentAnalysisResponse dto = new DocumentAnalysisResponse();
        dto.setDocumentId(analysis.getDocument().getId());
        dto.setOriginalFilename(
                analysis.getDocument().getOriginalFilename()
        );
        dto.setSummary(analysis.getSummary());
        dto.setKeyClauses(analysis.getKeyClauses());
        dto.setPartyObligations(analysis.getPartyObligations());
        dto.setRiskFactors(analysis.getRiskFactors());
        dto.setImprovements(analysis.getImprovements());
        dto.setModelUsed(analysis.getModelUsed());
        dto.setPromptVersion(analysis.getPromptVersion());
        dto.setCreatedAt(analysis.getCreatedAt());
        return dto;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyDocuments(Authentication authentication) {

        String userIdStr = (String) authentication.getPrincipal();

        User user = userRepo.findById(Long.parseLong(userIdStr))
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        var documents = documentService.findAllByUser(user);

        var response = documents.stream().map(doc -> {
            UserDocumentResponse dto = new UserDocumentResponse();
            dto.setId(doc.getId());
            dto.setOriginalFilename(doc.getOriginalFilename());
            dto.setUploadedAt(doc.getUploadedAt());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }



}