package com.telusko.springecom.repo;

import com.telusko.springecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // Spring Data JPA will automatically write the SQL query for this!
    Optional<User> findByEmail(String email);
}