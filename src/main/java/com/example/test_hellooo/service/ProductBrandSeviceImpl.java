package com.example.test_hellooo.service;

import com.example.test_hellooo.entity.ProductBrand;
import com.example.test_hellooo.repository.BrandRepository;
import com.example.test_hellooo.repository.ProductBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductBrandSeviceImpl implements ProductBrandService{
    private final BrandRepository brandRepository;
    private final ProductBrandRepository productBrandRepository;

    @Autowired
    public ProductBrandSeviceImpl(BrandRepository brandRepository, ProductBrandRepository productBrandRepository) {
        this.brandRepository = brandRepository;
        this.productBrandRepository = productBrandRepository;
    }


    @Override
    public int updateBrandForProduct(Long newBrandId, Long productId) {
        // Kiểm tra xem brand mới có tồn tại không
        if (!brandRepository.existsById(newBrandId.intValue())) {
            throw new EntityNotFoundException("Brand with ID " + newBrandId + " not found.");
        }
        // Gọi repository để thực hiện cập nhật
        int rowsAffected = productBrandRepository.updateBrandForProduct(newBrandId, productId);

        if (rowsAffected > 0) {
            System.out.println("Successfully updated brand for product with ID: " + productId);
        } else {
            System.out.println("No records updated for product with ID: " + productId);
        }

        return rowsAffected;
    }

}
