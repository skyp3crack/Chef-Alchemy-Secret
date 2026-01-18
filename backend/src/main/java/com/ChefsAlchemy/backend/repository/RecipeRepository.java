package com.ChefsAlchemy.backend.repository;

import org.springframework.stereotype.Repository;

import com.ChefsAlchemy.backend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//recipe repository act as an interface for recipe entity
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthorId(Long userId); // find recipes by specific user
}
