//package com.sparta.msa.auth.domain.repository;
//
//import com.sparta.msa.auth.domain.model.User;
//import jakarta.validation.constraints.NotNull;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, String> {
//    Optional<User> findByusername(String username);
//
//    boolean existsByUsername(String username);
//
//    boolean existsByEmail(String email);
//
//    boolean existsBySlackId(String slackId);
//}
