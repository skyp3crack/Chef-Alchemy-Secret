package com.ChefsAlchemy.backend.repository;

//this interface will handle database operations for user entity
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ChefsAlchemy.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // already include CRUD operations for JPARepository
    // option find by username,all,id,count,delete,exists
    // Spring turns this into: SELECT * FROM users WHERE username = ?
    Optional<User> findbyUsername(String username);

    Optional<User> findbyEmail(String email);

    Boolean existsbyUsername(String username);

    Boolean existsbyEmail(String email); // spring turns this into: SELECT COUNT(*) FROM users WHERE email = ?

}
