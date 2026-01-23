package com.ChefsAlchemy.backend.repository;

import com.ChefsAlchemy.backend.model.Recipe;
import com.ChefsAlchemy.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRecipe(Recipe recipe);
}
