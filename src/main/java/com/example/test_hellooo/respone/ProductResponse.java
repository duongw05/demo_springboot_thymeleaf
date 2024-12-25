package com.example.test_hellooo.respone;

import com.example.test_hellooo.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponse {

    private Long id;  // Giữ lại kiểu Integer
    private String productName;
    private String color;
    private Double originPrice;
    private Double sellPrice;
    private Integer quantity;
    private String description;
    private Integer subcategoryId;
    private String subcateName;
    private Integer statusId;
    private String statusName;
    private Long brandId;
    private String brandName;

    // Dành cho việc hiển thị danh sách sản phẩm (phân trang)
    private List<Product> products;
    private int currentPage;
    private int totalPages;
    private long totalEntries;

    // Constructor cho tìm kiếm hoặc trả về kết quả từng sản phẩm chi tiết
    public ProductResponse(
            Long id, String productName, String color, Double originPrice, Double sellPrice, Integer quantity,
            String description, Integer subcategoryId, String subcateName, Integer statusId,
            String statusName, Long brandId, String brandName) {
        this.id = id;
        this.productName = productName;
        this.color = color;
        this.originPrice = originPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.description = description;
        this.subcategoryId = subcategoryId;
        this.subcateName = subcateName;
        this.statusId = statusId;
        this.statusName = statusName;
        this.brandId = brandId;
        this.brandName = brandName;
    }

    // Constructor cho việc tạo đối tượng chứa dữ liệu phân trang
    public ProductResponse(List<Product> products, int currentPage, int totalPages, long totalEntries) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalEntries = totalEntries;
    }
}
