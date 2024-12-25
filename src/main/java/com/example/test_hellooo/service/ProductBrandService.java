package com.example.test_hellooo.service;

import com.example.test_hellooo.entity.ProductBrand;
import org.springframework.data.repository.query.Param;

public interface ProductBrandService {

    int updateBrandForProduct(Long newBrandId,Long productId);

}
