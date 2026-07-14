package com.aicodereview.service;

import com.aicodereview.entity.Project;
import com.aicodereview.entity.Review;
import com.aicodereview.entity.ReviewFinding;
import com.aicodereview.repository.ReviewRepository;
import com.aicodereview.repository.ReviewFindingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewFindingRepository reviewFindingRepository;

    public Review createReview(Project project, Integer score, String summary) {
        Review review = new Review();
        review.setProject(project);
        review.setReviewScore(score);
        review.setSummary(summary);
        return reviewRepository.save(review);
    }

    public ReviewFinding addFinding(Review review, String severity, String issue,
                                    String explanation, String suggestion,
                                    String fileName, Integer lineNumber) {
        ReviewFinding finding = new ReviewFinding();
        finding.setReview(review);
        finding.setSeverity(severity);
        finding.setIssue(issue);
        finding.setExplanation(explanation);
        finding.setSuggestion(suggestion);
        finding.setFileName(fileName);
        finding.setLineNumber(lineNumber);
        return reviewFindingRepository.save(finding);
    }

    public List<Review> getReviewsByProject(Long projectId) {
        return reviewRepository.findByProjectId(projectId);
    }

    public List<ReviewFinding> getFindingsByReview(Long reviewId) {
        return reviewFindingRepository.findByReviewId(reviewId);
    }
}