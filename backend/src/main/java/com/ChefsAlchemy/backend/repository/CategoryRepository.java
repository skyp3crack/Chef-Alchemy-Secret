
package com.ChefsAlchemy.backend.repository;

//after creating mode-> then repository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ChefsAlchemy.backend.model.Category;
import java.util.Optional;

@Repository // category repository used to perform database operations such as find, save,
            // delete, update. but in this case
            // we are using it to find a category by name and check if a category exists by
            // name
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    boolean existsByName(String name);

}
