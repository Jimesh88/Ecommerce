package com.productservice.product_service.repository;

import com.productservice.product_service.model.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends R2dbcRepository<Product,Long> {
}
