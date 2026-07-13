package com.aicodereview.controller;

import com.aicodereview.dto.AnalysisReportDto;
import com.aicodereview.entity.Project;
import com.aicodereview.entity.Review;
import com.aicodereview.repository.ProjectRepository;
import com.aicodereview.service.AnalysisReportService;
import com.aicodereview.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisReportController {

    @Autowired
    private AnalysisReportService analysisReportService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/full")
    public AnalysisReportDto getFullReport(
            @RequestParam String javaFilePath,
            @RequestParam String classFilePath,
            @RequestParam Long projectId) throws Exception {

        File javaFile = new File(javaFilePath);
        AnalysisReportDto report = analysisReportService.generateReport(javaFile, javaFilePath, classFilePath);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        Review review = reviewService.createReview(project, null, "Static analysis report for " + report.getFileName());

        for (String violation : report.getCheckstyleViolations()) {
            reviewService.addFinding(review, "CHECKSTYLE", violation, null, null, report.getFileName(), null);
        }
        for (String violation : report.getPmdViolations()) {
            reviewService.addFinding(review, "PMD", violation, null, null, report.getFileName(), null);
        }
        for (String violation : report.getSpotbugsViolations()) {
            reviewService.addFinding(review, "SPOTBUGS", violation, null, null, report.getFileName(), null);
        }

        return report;
    }
}