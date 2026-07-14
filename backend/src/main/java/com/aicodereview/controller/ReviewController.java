package com.aicodereview.controller;

import com.aicodereview.entity.Review;
import com.aicodereview.entity.ReviewFinding;
import com.aicodereview.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/project/{projectId}")
    public List<Review> getReviewsByProject(@PathVariable Long projectId) {
        return reviewService.getReviewsByProject(projectId);
    }

    @GetMapping("/{reviewId}/findings")
    public List<ReviewFinding> getFindingsByReview(@PathVariable Long reviewId) {
        return reviewService.getFindingsByReview(reviewId);

    }
}