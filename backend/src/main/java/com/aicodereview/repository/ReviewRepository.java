package com.aicodereview.repository;

import com.aicodereview.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProjectId(Long projectId);
    List<Review> findByProject_User_IdOrderByCreatedAtDesc(Long userId);
    @Query("SELECT r FROM Review r WHERE r.project.user.id = :userId " +
            "AND (LOWER(r.project.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY r.createdAt DESC")
    List<Review> searchReviews(@Param("userId") Long userId, @Param("keyword") String keyword);
}