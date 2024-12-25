package com.example.test_hellooo.repository;

import com.example.test_hellooo.entity.Product;
import com.example.test_hellooo.request.ProductRequest;
import com.example.test_hellooo.respone.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    // Tìm kiếm sản phẩm theo tên
    List<Product> findByProductNameContainingIgnoreCase(String productName);

    // Tìm sản phẩm theo trạng thái
    List<Product> findByStatusId(Integer statusId);

    // Tìm sản phẩm theo subcategory

    // Tìm sản phẩm theo khoảng giá bán
    List<Product> findBySellPrice(Double Price);

    // Lấy tất cả sản phẩm cùng với tên thương hiệu
    @Query("SELECT new com.example.test_hellooo.respone.ProductResponse(" +
            "p.id, p.productName, p.color, p.originPrice, p.sellPrice, p.quantity, p.description, " +
            "p.subcate.id, p.subcate.subCateName, " +
            "p.status.id, p.status.statusName, " +
            " pb.brand.id, pb.brand.brandName) " +  // Lấy id và tên thương hiệu
            "FROM Product p " +
            "LEFT JOIN p.productBrands pb "+
            "LEFT JOIN pb.brand b " +  // JOIN brands vào
            "LEFT JOIN p.subcate s " +  // JOIN subcategory vào
            "LEFT JOIN p.status st")  // JOIN status vào
    Page<ProductResponse> getAllProductsWithBrand(Pageable pageable);



    @Query("SELECT new com.example.test_hellooo.respone.ProductResponse(" +
            "p.id, p.productName, p.color, p.originPrice, p.sellPrice, p.quantity, " +
            "p.description, p.subcate.id, p.subcate.subCateName, " +
            "p.status.id, p.status.statusName, pb.brand.id, pb.brand.brandName) " +
            "FROM Product p " +
            "LEFT JOIN p.productBrands pb " +  // Join với bảng ProductBrand
            "LEFT JOIN pb.brand b " +  // Join với bảng Brand thông qua ProductBrand
            "WHERE (:#{#request.productName} IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :#{#request.productName}, '%'))) " +
            "AND (:#{#request.sellPrice} IS NULL OR p.sellPrice = :#{#request.sellPrice}) " +
            "AND (:#{#request.brandId} IS NULL OR pb.brand.id = :#{#request.brandId}) " +
            "AND (:#{#request.subcategoryId} IS NULL OR p.subcate.id = :#{#request.subcategoryId}) " +
            "AND (:#{#request.statusId} IS NULL OR p.status.id = :#{#request.statusId})")
    Page<ProductResponse> searchProductsPage(
            @Param("request") ProductRequest request,
            Pageable pageable);

}
