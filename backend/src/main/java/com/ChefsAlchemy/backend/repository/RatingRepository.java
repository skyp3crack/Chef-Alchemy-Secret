
package com.ChefsAlchemy.backend.repository;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ChefsAlchemy.backend.model.Recipe;
import com.ChefsAlchemy.backend.model.User;
import com.ChefsAlchemy.backend.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndRecipe(User user, Recipe recipe);

    boolean existsByUserAndRecipe(User user, Recipe recipe);

    // add methods to calculate average rating for recipe
    List<Rating> findByRecipe(Recipe recipe);

}
