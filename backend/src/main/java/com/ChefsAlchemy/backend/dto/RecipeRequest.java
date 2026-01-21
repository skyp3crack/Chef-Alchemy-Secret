package com.ChefsAlchemy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

//recipe request dto as a data transfer 
//after created model->reposioty->dto
//dto is used to transfer data between layers as 
// in this case we used to store ids of categories and tags in recipe request dto
public class RecipeRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    @Size(min = 3)
    private String description;

    @NotBlank
    @Size(min = 3)
    private String ingredients;

    @NotBlank
    @Size(min = 3)
    private String instructions;

    private String imageUrl;

    private List<String> tagsIds;
    private List<String> categoriesIds;

    public RecipeRequest() {
    }

    public RecipeRequest(String title, String description, String ingredients, String instructions, String imageUrl,
            List<String> tagsIds, List<String> categoriesIds) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.tagsIds = tagsIds;
        this.categoriesIds = categoriesIds;
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

    public void setTagsIds(List<String> tagsIds) {
        this.tagsIds = tagsIds;
    }

    public void setCategoriesIds(List<String> categoriesIds) {
        this.categoriesIds = categoriesIds;
    }

    public List<String> getTagsIds() {
        return tagsIds;
    }

    public List<String> getCategoriesIds() {
        return categoriesIds;
    }

}