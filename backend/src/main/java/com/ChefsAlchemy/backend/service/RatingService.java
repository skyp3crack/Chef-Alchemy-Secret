package com.ChefsAlchemy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentAuthenticatedUSer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found in database"));
    }

    @Transactional
    public void submitRating(Long recipeId, RatingRequest ratingRequest) {
        User currentUser = getCurrentAuthenticatedUser();
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found with id: " + recipeId));

        Optional<Rating> existingRating = ratingRepository.findByUserAndRecipe(currentUser, recipe);

        Rating rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
            rating.setScore(ratingRequest.getScore());
        } else {
            rating = new Rating(currentUser, recipe, ratingRequest.getScore());
        }
        ratingRepository.save(rating);

        // Recalculate average rating for the recipe
        updateRecipeAverageRating(recipe);
    }

    @Transactional
    private void updateRecipeAverageRating(Recipe recipe) {
        Double averageScore = ratingRepository.findByRecipe(recipe).stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
        Long count = ratingRepository.findByRecipe(recipe).stream().count();
        recipe.setAverageRating(averageScore);
        recipe.setRatingCount(count);
        recipeRepository.save(recipe);
    }

    @Transactional
    public Optional<Rating> getUserRatingForRecipe(Long recipeId) {
        User currentUser = getCurrentAuthenticatedUser();
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + recipeId));
        return ratingRepository.findByUserAndRecipe(currentUser, recipe);
    }
}
