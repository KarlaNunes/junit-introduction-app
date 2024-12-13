package com.ifrn.junitintroductionapp.repository;

import com.ifrn.junitintroductionapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
