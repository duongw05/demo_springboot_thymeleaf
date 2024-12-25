package com.example.test_hellooo.request;

import com.example.test_hellooo.entity.Brand;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {
    private Long id;
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String productName;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Original price is required")
    @Positive(message = "Original price must be positive")
    private Double originPrice;

    @NotNull(message = "Selling price is required")
    @Positive(message = "Selling price must be positive")
    private Double sellPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    @Size(max = 500, message = "Description can be up to 500 characters long")
    private String description;

    @NotNull(message = "Subcategory ID is required")
    private Integer subcategoryId;

    @NotNull(message = "Status ID is required")
    private Integer statusId;

    @NotNull(message = "Brand IDs are required")
    private Integer brandId;
    private Integer page; // Default value is 0 if no value provided

    private Integer size; // Default value is 10 if no value provided


    // Constructor nhận vào các tham số tương ứng với kết quả truy vấn JPQL
    public ProductRequest(String productName, String color, Double originPrice,
                          Double sellPrice, Integer quantity, String description,
                          Integer subcategoryId, Integer statusId, Integer brandId) {
        this.productName = productName;
        this.color = color;
        this.originPrice = originPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.description = description;
        this.subcategoryId = subcategoryId;
        this.statusId = statusId;
        this.brandId = brandId;
    }

}
