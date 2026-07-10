package com.aicodereview.controller;

import com.aicodereview.service.SpotBugsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spotbugs")
public class SpotBugsController {

    private final SpotBugsService spotBugsService;

    public SpotBugsController(SpotBugsService spotBugsService) {
        this.spotBugsService = spotBugsService;
    }

    @GetMapping("/test")
    public List<SpotBugsService.SpotBugsIssue> testAnalyze(@RequestParam String classFilePath) throws Exception {
        return spotBugsService.analyze(classFilePath);
    }
}