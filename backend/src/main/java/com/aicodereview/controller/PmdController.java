package com.aicodereview.controller;

import com.aicodereview.service.PmdService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/pmd")
public class PmdController {

    private final PmdService pmdService;

    public PmdController(PmdService pmdService) {
        this.pmdService = pmdService;
    }

    @GetMapping("/test")
    public List<PmdService.PmdIssue> testAnalyze(@RequestParam String filePath) {
        return pmdService.analyze(filePath);
    }
}
