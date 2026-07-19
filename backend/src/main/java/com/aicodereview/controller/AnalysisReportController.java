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
            @RequestParam Long projectId) throws Exception {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        String javaFilePath = project.getFilePath();
        String classFilePath = javaFilePath; // TODO: revisit once SpotBugs needs a real .class path

        File javaFile = new File(javaFilePath);
        AnalysisReportDto report = analysisReportService.generateReport(javaFile, javaFilePath, classFilePath);

        int score = 100;
        score -= report.getCheckstyleViolations().size() * 2;
        score -= report.getPmdViolations().size() * 3;
        score -= report.getSpotbugsViolations().size() * 5;
        score = Math.max(score, 0);
        Review review = reviewService.createReview(project, score, "Static analysis report for " + report.getFileName());

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