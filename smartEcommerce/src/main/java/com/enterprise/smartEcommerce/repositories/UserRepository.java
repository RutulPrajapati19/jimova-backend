package com.enterprise.smartEcommerce.repositories;

import com.enterprise.smartEcommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA is smart enough to write the SQL query automatically just by reading this method name!
    // Equivalent SQL: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Checks if an email already exists in the database
    boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);
}