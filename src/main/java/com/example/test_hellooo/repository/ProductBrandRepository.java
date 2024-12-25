package com.example.test_hellooo.repository;

import com.example.test_hellooo.entity.ProductBrand;
import com.example.test_hellooo.entity.ProductBrandId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, ProductBrandId> {
    // Tìm ProductBrand dựa trên productId trong ProductBrandId
    ProductBrand findByIdProductId(Integer productId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE product_brand SET brand_id = :newBrandId WHERE product_id = :productId", nativeQuery = true)
    int updateBrandForProduct(@Param("newBrandId") Long newBrandId,
                              @Param("productId") Long productId);


}
