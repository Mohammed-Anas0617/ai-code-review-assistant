package com.aicodereview.controller;

import com.aicodereview.dto.AnalysisReportDto;
import com.aicodereview.service.AnalysisReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisReportController {

    @Autowired
    private AnalysisReportService analysisReportService;

    @GetMapping("/full")
    public AnalysisReportDto getFullReport(
            @RequestParam String javaFilePath,
            @RequestParam String classFilePath) throws Exception {

        File javaFile = new File(javaFilePath);
        return analysisReportService.generateReport(javaFile, javaFilePath, classFilePath);
    }
}