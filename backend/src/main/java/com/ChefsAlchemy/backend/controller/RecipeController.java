
package com.ChefsAlchemy.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Only authenticated users can create
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        try {
            RecipeResponse newRecipe = recipeService.createRecipe(recipeRequest);
            return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Not authenticated or other auth issue
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAllRecipes() {
        List<RecipeResponse> recipes = recipeService.getAllRecipes(); //
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable Long id) {
        try {
            RecipeResponse recipe = recipeService.getRecipeById(id);
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Only authenticated users can update
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable Long id,
            @Valid @RequestBody RecipeRequest recipeRequest) {
        try {
            RecipeResponse updatedRecipe = recipeService.updateRecipe(id, recipeRequest);
            return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Not authorized to update this recipe
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Only authenticated users can delete
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipe(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Not authorized to delete this recipe
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}