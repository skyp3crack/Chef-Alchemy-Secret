package com.ChefsAlchemy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReviewRequest {
    @NotBlank
    @Size(min = 5, max = 1000)
    private String comment;

    // Constructors
    public ReviewRequest() {
    }

    public ReviewRequest(String comment) {
        this.comment = comment;
    }

    // Getters and Setters
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
