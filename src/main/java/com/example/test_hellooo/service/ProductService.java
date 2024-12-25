package com.example.test_hellooo.service;

import com.example.test_hellooo.request.ProductRequest;
import com.example.test_hellooo.respone.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse saveProduct(ProductRequest productRequest);

//    List<ProductResponse> getAllProduct();
    List<ProductResponse> getProductByName(String productName);
    List<ProductResponse> getProductByStatus(Integer statusId);
//    List<ProductResponse> getProductBySubcategory(Integer subcateId);
    List<ProductResponse> getProductBySellPrice(Double sellPrice);
    ProductResponse findProductById(Integer id);

    Page<ProductResponse> getAllProductsWithBrand(Pageable pageable);


    ProductResponse updateProductById(ProductRequest productRequest, Integer id);

    Page<ProductResponse> searchProductsPage(ProductRequest productRequest, Pageable pageable); // Dùng ProductRequest ở đây
}
