package com.ChefsAlchemy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.transaction.Transactional;
import java.util.List;
import com.ChefsAlchemy.backend.dto.RecipeRequest;
import com.ChefsAlchemy.backend.dto.RecipeResponse;
import com.ChefsAlchemy.backend.model.Recipe;
import com.ChefsAlchemy.backend.model.User;
import com.ChefsAlchemy.backend.repository.RecipeRepository;
import com.ChefsAlchemy.backend.repository.UserRepository;
import com.ChefsAlchemy.backend.security.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

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

    private RecipeResponse convertToDto(Recipe recipe) { // convert recipe to dto for response
        return new RecipeResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                recipe.getAuthor().getId(),
                recipe.getAuthor().getUsername(),
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
        Recipe savedRecipe = recipeRepository.save(recipe);
        return convertToDto(savedRecipe);

    }

    @Transactional
    public List<RecipeResponse> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
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
