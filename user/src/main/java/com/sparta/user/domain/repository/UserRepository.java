package com.sparta.user.domain.repository;//package com.sparta.msa.auth.domain.repository;

import com.sparta.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsBySlackId(String slackId);
}
