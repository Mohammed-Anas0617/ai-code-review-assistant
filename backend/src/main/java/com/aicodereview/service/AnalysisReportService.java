package com.aicodereview.service;

import com.aicodereview.dto.AnalysisReportDto;
import com.aicodereview.service.PmdService.PmdIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisReportService {

    @Autowired
    private CheckstyleService checkstyleService;

    @Autowired
    private PmdService pmdService;

    @Autowired
    private SpotBugsService spotBugsService;

    public AnalysisReportDto generateReport(File javaFile, String filePath, String classFilePath) throws Exception {

        AnalysisReportDto report = new AnalysisReportDto();
        report.setFileName(javaFile.getName());

        // Checkstyle
        report.setCheckstyleViolations(checkstyleService.analyze(javaFile));

        // PMD
        List<String> pmdResults = new ArrayList<>();
        for (PmdService.PmdIssue issue : pmdService.analyze(filePath)) {
            pmdResults.add(issue.ruleName() + " (Line " + issue.line() + "): " + issue.message());
        }
        report.setPmdViolations(pmdResults);

        // SpotBugs
        List<String> spotbugsResults = new ArrayList<>();
        for (SpotBugsService.SpotBugsIssue issue : spotBugsService.analyze(classFilePath)) {
            spotbugsResults.add(issue.type() + " (Line " + issue.line() + "): " + issue.message());
        }
        report.setSpotbugsViolations(spotbugsResults);

        return report;
    }
}