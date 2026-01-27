package com.ChefsAlchemy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile image;
    private Boolean removeImage; // Flag to indicate image removal

    private List<Long> tagIds;
    private List<Long> categoryIds;

    public RecipeRequest() {
    }

    public RecipeRequest(String title, String description, String ingredients, String instructions, MultipartFile image,
            List<Long> tagIds, List<Long> categoryIds) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.image = image;
        this.tagIds = tagIds;
        this.categoryIds = categoryIds;
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image; // store image in dto
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public Boolean getRemoveImage() {
        return removeImage;
    }

    public void setRemoveImage(Boolean removeImage) {
        this.removeImage = removeImage;
    }

}