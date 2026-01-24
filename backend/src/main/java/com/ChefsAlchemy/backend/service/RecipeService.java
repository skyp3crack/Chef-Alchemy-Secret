package com.ChefsAlchemy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.Optional;

import com.ChefsAlchemy.backend.dto.RecipeRequest;
import com.ChefsAlchemy.backend.dto.RecipeResponse;
import com.ChefsAlchemy.backend.dto.ReviewResponse;
import com.ChefsAlchemy.backend.dto.CategoryResponse;
import com.ChefsAlchemy.backend.dto.TagResponse;
import com.ChefsAlchemy.backend.model.Recipe;
import com.ChefsAlchemy.backend.model.User;
import com.ChefsAlchemy.backend.model.Category;
import com.ChefsAlchemy.backend.model.Tag;
import com.ChefsAlchemy.backend.repository.RecipeRepository;
import com.ChefsAlchemy.backend.repository.UserRepository;
import com.ChefsAlchemy.backend.repository.CategoryRepository;
import com.ChefsAlchemy.backend.repository.TagRepository;
import com.ChefsAlchemy.backend.security.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JwtUtils jwtUtils; // getting username from token if needed.

    // get the current authenticated user
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found in database."));
    }

    private ReviewResponse convertReviewToDto(com.ChefsAlchemy.backend.model.Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getRecipe().getId(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

    private RecipeResponse convertToDto(Recipe recipe) { // convert recipe to dto for response

        Set<CategoryResponse> categories = recipe.getCategories().stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toSet());
        Set<TagResponse> tags = recipe.getTags().stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toSet());

        // convert reviews to reviewresponse DTOs
        List<ReviewResponse> reviewResponses = recipe.getReviews().stream()
                .map(this::convertReviewToDto)
                .collect(Collectors.toList());

        return new RecipeResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                recipe.getAuthor().getId(),
                recipe.getAuthor().getUsername(),
                categories,
                tags,
                recipe.getAverageRating(),
                recipe.getRatingCount(),
                reviewResponses,
                recipe.getCreatedAt(),
                recipe.getUpdatedAt());
    }

    @Transactional // transactional used to ensure data consistency
    public RecipeResponse createRecipe(RecipeRequest recipeRequest) { // create recipe
        User currentUser = getCurrentUser();
        Recipe recipe = new Recipe(
                recipeRequest.getTitle(),
                recipeRequest.getDescription(),
                recipeRequest.getIngredients(),
                recipeRequest.getInstructions(),
                recipeRequest.getImageUrl(),
                currentUser);

        // handle tags and categories
        if (recipeRequest.getCategoryIds() != null && !recipeRequest.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(
                    categoryRepository.findAllById(recipeRequest.getCategoryIds()));
            recipe.setCategories(categories);
        }

        if (recipeRequest.getTagIds() != null && !recipeRequest.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(
                    tagRepository.findAllById(recipeRequest.getTagIds()));
            recipe.setTags(tags);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);
        return convertToDto(savedRecipe);

    }

    @Transactional
    public List<RecipeResponse> getAllRecipes(String keyword, List<Long> categoryIds, List<Long> tagIds) {
        List<Recipe> recipes;
        if (keyword != null
                || (categoryIds != null && !categoryIds.isEmpty() || (tagIds != null && !tagIds.isEmpty()))) {
            recipes = recipeRepository.searchAndFilterRecipes(keyword, categoryIds, tagIds);
        } else {
            recipes = recipeRepository.findAll();
        }
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList()); // convert to dto for response
    }

    @Transactional
    public RecipeResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + id));
        return convertToDto(recipe);
    }

    @Transactional
    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        User currentUser = getCurrentUser();
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + id));

        // Ensure only the author can update their recipe
        if (!existingRecipe.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to update this recipe.");
        }

        existingRecipe.setTitle(recipeRequest.getTitle());
        existingRecipe.setDescription(recipeRequest.getDescription());
        existingRecipe.setIngredients(recipeRequest.getIngredients());
        existingRecipe.setInstructions(recipeRequest.getInstructions());
        existingRecipe.setImageUrl(recipeRequest.getImageUrl());

        // Update Categories
        if (recipeRequest.getCategoryIds() != null) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(recipeRequest.getCategoryIds()));
            existingRecipe.setCategories(categories);
        } else {
            existingRecipe.setCategories(new HashSet<>()); // Clear if no categories provided
        }

        // Update Tags
        if (recipeRequest.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(recipeRequest.getTagIds()));
            existingRecipe.setTags(tags);
        } else {
            existingRecipe.setTags(new HashSet<>()); // Clear if no tags provided
        }

        Recipe updatedRecipe = recipeRepository.save(existingRecipe);
        return convertToDto(updatedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        User currentUser = getCurrentUser();
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + id));

        // Ensure only the author can delete their recipe
        if (!existingRecipe.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to delete this recipe.");
        }

        recipeRepository.delete(existingRecipe);
    }

}
