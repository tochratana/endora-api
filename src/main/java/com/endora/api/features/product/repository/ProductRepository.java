package com.endora.api.features.product.repository;

import com.endora.api.features.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Modifying
    @Query("DELETE FROM Product p")
    void deleteAllProducts();

    @Query("SELECT COUNT(p) FROM Product p")
    long countProducts();

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}