package com.intellias.testmarketplace.repository;

import com.intellias.testmarketplace.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<Product, UUID> {
    @Query("FROM Product p")
    Iterable<Product> findAll();

    @Query("FROM Product p WHERE p.id = (:id)")
    Optional<Product> findById(@Param("id") UUID id);

    @Query("FROM Product p WHERE p.name = (:name)")
    Optional<Product> findByName(@Param("name") String name);

    @Query("FROM Product p LEFT JOIN FETCH p.users u WHERE u.id = (:id)")
    Iterable<Product> findByUserId(@Param("id") UUID id);
}
