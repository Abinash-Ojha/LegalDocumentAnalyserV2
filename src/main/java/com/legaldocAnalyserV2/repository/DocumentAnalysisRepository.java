package com.legaldocAnalyserV2.repository;

import com.legaldocAnalyserV2.model.DocumentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentAnalysisRepository extends JpaRepository<DocumentAnalysis,Long> {
}
