
package com.ChefsAlchemy.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class RecipeResponse {
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private String imageUrl;
    private Long authorId;
    private String authorUsername;
    private Set<CategoryResponse> categories;
    private Set<TagResponse> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecipeResponse() {
    }

    public RecipeResponse(Long id, String title, String description, String ingredients, String instructions,
            String imageUrl, Long authorId, String authorUsername,
            Set<CategoryResponse> categories, Set<TagResponse> tags,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.categories = categories;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public Set<CategoryResponse> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryResponse> categories) {
        this.categories = categories;
    }

    public Set<TagResponse> getTags() {
        return tags;
    }

    public void setTags(Set<TagResponse> tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
