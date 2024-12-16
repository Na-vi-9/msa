package com.sparta.msa.product.domain.repository;

import com.sparta.msa.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByUuidAndIsDeletedFalse(UUID uuid);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND " +
           "(:condition IS NULL OR p.name LIKE %:keyword%)")
    Page<Product> findAllWithCondition(@Param("condition") String condition,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);
}
