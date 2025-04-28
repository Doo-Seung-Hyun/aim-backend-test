package com.aim.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aim.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
}
