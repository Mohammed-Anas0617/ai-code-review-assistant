package com.aicodereview.service;

import com.aicodereview.entity.Project;
import com.aicodereview.entity.Review;
import com.aicodereview.entity.ReviewFinding;
import com.aicodereview.repository.ReviewRepository;
import com.aicodereview.repository.ReviewFindingRepository;
import com.aicodereview.repository.UserRepository;
import com.aicodereview.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewFindingRepository reviewFindingRepository;

    @Autowired
    private UserRepository userRepository;

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

    public List<Review> getReviewHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepository.findByProject_User_IdOrderByCreatedAtDesc(user.getId());
    }

    public List<Review> searchReviewHistory(String email, String keyword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepository.searchReviews(user.getId(), keyword);
    }
}