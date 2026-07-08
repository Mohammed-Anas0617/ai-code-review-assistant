package com.aicodereview.controller;

import com.aicodereview.service.CheckstyleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final CheckstyleService checkstyleService;

    public AnalysisController(CheckstyleService checkstyleService) {
        this.checkstyleService = checkstyleService;
    }

    @PostMapping("/checkstyle")
    public Map<String, Object> analyzeFile(@RequestParam("file") MultipartFile file) throws Exception {

        // Save uploaded file temporarily to disk (Checkstyle needs a real File, not a stream)
        Path tempFile = Files.createTempFile("upload-", "-" + file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        List<String> violations;
        try {
            violations = checkstyleService.analyze(tempFile.toFile());
        } finally {
            Files.deleteIfExists(tempFile); // clean up after analysis
        }

        return Map.of(
                "fileName", file.getOriginalFilename(),
                "violationCount", violations.size(),
                "violations", violations
        );
    }
}