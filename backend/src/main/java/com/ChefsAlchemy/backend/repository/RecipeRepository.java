package com.ChefsAlchemy.backend.repository;

import org.springframework.stereotype.Repository;

import com.ChefsAlchemy.backend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//recipe repository act as an interface for recipe entity
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthorId(Long userId); // find recipes by specific user

    // Search by keyword in title description or ingredients
    @Query("SELECT r FROM Recipe r WHERE " +
            "(:keyword is null or LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword,'%')) OR " +
            "LOWER(r.description) LIKE LOWER( CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.ingredients) LIKE LOWER( CONCAT('%', :keyword, '%')))")
    List<Recipe> searchByKeyword(@Param("keyword") String keyword);

    // filter by category IDs
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.categories c WHERE c.id IN :categoryIds")
    List<Recipe> findByCategoriesIdIn(@Param("categoryIds") List<Long> categoryIds);

    // filter by tag IDs
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.tags t WHERE t.id IN :tagIds")
    List<Recipe> findByTagsIdIn(@Param("tagIds") List<Long> tagIds);

    // Combine search and filter
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "LEFT JOIN r.categories c " +
            "LEFT JOIN r.tags t " +
            "WHERE (:keyword = '' OR (" +
            "LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :keyword, '%')))) " +
            "AND (:categoryIds IS NULL OR c.id IN (:categoryIds)) " +
            "AND (:tagIds IS NULL OR t.id IN (:tagIds))")
    List<Recipe> searchAndFilterRecipes(
            @Param("keyword") String keyword,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("tagIds") List<Long> tagIds);

}
