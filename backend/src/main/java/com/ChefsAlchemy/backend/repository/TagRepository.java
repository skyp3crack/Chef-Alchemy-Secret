package com.ChefsAlchemy.backend.repository;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ChefsAlchemy.backend.model.Tag;

//this reposity we used to find tag by name and check if tag exists by name
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    boolean existsByName(String name);
}
