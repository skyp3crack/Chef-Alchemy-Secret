
package com.ChefsAlchemy.backend.service;

import com.ChefsAlchemy.backend.dto.ReviewRequest;
import com.ChefsAlchemy.backend.dto.ReviewResponse;
import com.ChefsAlchemy.backend.model.Recipe;
import com.ChefsAlchemy.backend.model.Review;
import com.ChefsAlchemy.backend.model.User;
import com.ChefsAlchemy.backend.repository.RecipeRepository;
import com.ChefsAlchemy.backend.repository.ReviewRepository;
import com.ChefsAlchemy.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found in database."));
    }

    private ReviewResponse convertToDto(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getRecipe().getId(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

    @Transactional
    public ReviewResponse addReview(Long recipeId, ReviewRequest reviewRequest) {
        User currentUser = getCurrentAuthenticatedUser();
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + recipeId));

        Review review = new Review(currentUser, recipe, reviewRequest.getComment());
        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest) {
        User currentUser = getCurrentAuthenticatedUser();
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        if (!existingReview.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to update this review.");
        }

        existingReview.setComment(reviewRequest.getComment());
        Review updatedReview = reviewRepository.save(existingReview);
        return convertToDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        User currentUser = getCurrentAuthenticatedUser();
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        if (!existingReview.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to delete this review.");
        }

        reviewRepository.delete(existingReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + recipeId));
        return reviewRepository.findByRecipe(recipe).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
