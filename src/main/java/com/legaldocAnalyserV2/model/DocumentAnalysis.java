package com.legaldocAnalyserV2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_analysis")
public class DocumentAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "document_id", unique = true)
    private Document document;


    @Column(nullable = false,columnDefinition = "TEXT")
    private String summary;


    @Column(nullable = false,columnDefinition = "TEXT")
    private String keyClauses;


    @Column(nullable = false,columnDefinition = "TEXT")
    private String partyObligations;


    @Column(nullable = false,columnDefinition = "TEXT")
    private String riskFactors;


    @Column(nullable = false,columnDefinition = "TEXT")
    private String improvements;

    @Column(nullable = false)
    private String modelUsed;

    @Column(nullable = false)
    private String promptVersion;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    // getters & setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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


