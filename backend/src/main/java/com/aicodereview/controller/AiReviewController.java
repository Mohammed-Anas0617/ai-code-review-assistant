package com.aicodereview.controller;

import com.aicodereview.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiReviewController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/review")
    public String reviewCode(@RequestBody String code) {
        return openAiService.reviewCode(code);
    }
}