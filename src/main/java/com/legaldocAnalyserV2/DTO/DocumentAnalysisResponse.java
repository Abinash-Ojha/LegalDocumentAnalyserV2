package com.legaldocAnalyserV2.DTO;

import java.time.LocalDateTime;

public class DocumentAnalysisResponse {

    private Long documentId;
    private String originalFilename;

    private String summary;
    private String keyClauses;
    private String partyObligations;
    private String riskFactors;
    private String improvements;

    private String modelUsed;
    private String promptVersion;
    private LocalDateTime createdAt;

    // getters & setters


    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKeyClauses() {
        return keyClauses;
    }

    public void setKeyClauses(String keyClauses) {
        this.keyClauses = keyClauses;
    }

    public String getPartyObligations() {
        return partyObligations;
    }

    public void setPartyObligations(String partyObligations) {
        this.partyObligations = partyObligations;
    }

    public String getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(String riskFactors) {
        this.riskFactors = riskFactors;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public String getPromptVersion() {
        return promptVersion;
    }

    public void setPromptVersion(String promptVersion) {
        this.promptVersion = promptVersion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
