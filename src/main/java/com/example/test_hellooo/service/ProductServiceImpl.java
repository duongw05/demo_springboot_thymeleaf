package com.example.test_hellooo.service;

import com.example.test_hellooo.entity.Brand;
import com.example.test_hellooo.entity.Product;
import com.example.test_hellooo.entity.ProductBrand;
import com.example.test_hellooo.entity.ProductBrandId;
import com.example.test_hellooo.entity.Status;
import com.example.test_hellooo.entity.Subcategory;
import com.example.test_hellooo.repository.BrandRepository;
import com.example.test_hellooo.repository.ProductBrandRepository;
import com.example.test_hellooo.repository.ProductRepository;
import com.example.test_hellooo.repository.StatusRepository;
import com.example.test_hellooo.repository.SubcategoryRepository;
import com.example.test_hellooo.request.ProductRequest;
import com.example.test_hellooo.respone.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final StatusRepository statusRepository;
    private final BrandRepository brandRepository;
    private final ProductBrandRepository productBrandRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              SubcategoryRepository subcategoryRepository,
                              StatusRepository statusRepository,
                              BrandRepository brandRepository, ProductBrandRepository productBrandRepository) {
        this.productRepository = productRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.statusRepository = statusRepository;
        this.brandRepository = brandRepository;
        this.productBrandRepository = productBrandRepository;
    }

    @Override
    public ProductResponse saveProduct(ProductRequest productRequest) {
        // Tạo đối tượng Product mới
        Product product = new Product();

        // Cập nhật các thuộc tính của sản phẩm từ ProductRequest
        product.setProductName(productRequest.getProductName());
        product.setColor(productRequest.getColor());
        product.setOriginPrice(productRequest.getOriginPrice());
        product.setSellPrice(productRequest.getSellPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setDescription(productRequest.getDescription());

        // Thiết lập subcategory nếu có
        if (productRequest.getSubcategoryId() != null) {
            Subcategory subcategory = subcategoryRepository.findById(productRequest.getSubcategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Subcategory not found with ID: " + productRequest.getSubcategoryId()));
            product.setSubcate(subcategory);
        }

        // Thiết lập status nếu có
        if (productRequest.getStatusId() != null) {
            Status status = statusRepository.findById(productRequest.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + productRequest.getStatusId()));
            product.setStatus(status);
        }

        // Lưu sản phẩm trước để đảm bảo ID được tạo ra (cần thiết cho mối quan hệ)
        Product savedProduct = productRepository.save(product);

        // Thiết lập brand nếu có
        if (productRequest.getBrandId() != null) {
            Brand brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found with ID: " + productRequest.getBrandId()));

            // Tạo ProductBrandId để thiết lập quan hệ
            ProductBrandId productBrandId = new ProductBrandId();
            productBrandId.setProductId(savedProduct.getId());
            productBrandId.setBrandId(brand.getId());

            // Tạo ProductBrand và thiết lập khóa hợp thành
            ProductBrand productBrand = new ProductBrand();
            productBrand.setId(productBrandId);  // Sử dụng khóa hợp thành
            productBrand.setBrand(brand);
            productBrand.setProduct(savedProduct);

            // Lưu ProductBrand vào cơ sở dữ liệu
            productBrandRepository.save(productBrand);

            // Cập nhật danh sách productBrands của sản phẩm (nếu cần thiết)
            savedProduct.getProductBrands().add(productBrand);
        }

        // Lấy thông tin thương hiệu liên kết với sản phẩm
        ProductBrand productBrand = savedProduct.getProductBrands().stream().findFirst().orElse(null);
        Long brandId = null;
        String brandName = null;
        if (productBrand != null) {
            brandId = productBrand.getBrand().getId();
            brandName = productBrand.getBrand().getBrandName();
        }

        // Trả về đối tượng ProductResponse
        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getProductName(),
                savedProduct.getColor(),
                savedProduct.getOriginPrice(),
                savedProduct.getSellPrice(),
                savedProduct.getQuantity(),
                savedProduct.getDescription(),
                savedProduct.getSubcate() != null ? savedProduct.getSubcate().getId() : null,
                savedProduct.getSubcate() != null ? savedProduct.getSubcate().getSubCateName() : null,
                savedProduct.getStatus() != null ? savedProduct.getStatus().getId() : null,
                savedProduct.getStatus() != null ? savedProduct.getStatus().getStatusName() : null,
                brandId,
                brandName
        );
    }

    @Override
    public List<ProductResponse> getProductByName(String productName) {
        // Tìm kiếm sản phẩm theo tên, không phân biệt chữ hoa, chữ thường
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(productName);

        // Lặp qua các sản phẩm và trả về danh sách ProductResponse
        return products.stream().map(product -> {
            // Lấy thông tin thương hiệu (nếu có)
            ProductBrand productBrand = product.getProductBrands().stream().findFirst().orElse(null);
            Long brandId = null;
            String brandName = null;
            if (productBrand != null) {
                brandId = productBrand.getBrand().getId();
                brandName = productBrand.getBrand().getBrandName();
            }

            // Tạo ProductResponse và trả về
            return new ProductResponse(
                    product.getId(),
                    product.getProductName(),
                    product.getColor(),
                    product.getOriginPrice(),
                    product.getSellPrice(),
                    product.getQuantity(),
                    product.getDescription(),
                    product.getSubcate() != null ? product.getSubcate().getId() : null,
                    product.getSubcate() != null ? product.getSubcate().getSubCateName() : null,
                    product.getStatus() != null ? product.getStatus().getId() : null,
                    product.getStatus() != null ? product.getStatus().getStatusName() : null,
                    brandId,    // Trả về BrandId duy nhất
                    brandName   // Trả về BrandName duy nhất
            );
        }).toList();
    }


    @Override
    public List<ProductResponse> getProductByStatus(Integer statusId) {
        // Tìm sản phẩm theo statusId
        List<Product> products = productRepository.findByStatusId(statusId);

        // Lặp qua các sản phẩm và trả về danh sách ProductResponse
        return products.stream().map(product -> {
            // Lấy thông tin thương hiệu (nếu có)
            ProductBrand productBrand = product.getProductBrands().stream().findFirst().orElse(null);
            Long brandId = null;
            String brandName = null;
            if (productBrand != null) {
                brandId = productBrand.getBrand().getId();
                brandName = productBrand.getBrand().getBrandName();
            }

            // Tạo ProductResponse và trả về
            return new ProductResponse(
                    product.getId(),
                    product.getProductName(),
                    product.getColor(),
                    product.getOriginPrice(),
                    product.getSellPrice(),
                    product.getQuantity(),
                    product.getDescription(),
                    product.getSubcate() != null ? product.getSubcate().getId() : null,
                    product.getSubcate() != null ? product.getSubcate().getSubCateName() : null,
                    product.getStatus() != null ? product.getStatus().getId() : null,
                    product.getStatus() != null ? product.getStatus().getStatusName() : null,
                    brandId,    // Trả về BrandId duy nhất
                    brandName   // Trả về BrandName duy nhất
            );
        }).toList();
    }


    @Override
    public List<ProductResponse> getProductBySellPrice(Double sellPrice) {
        // Tìm sản phẩm theo giá bán (sellPrice)
        List<Product> products = productRepository.findBySellPrice(sellPrice);

        // Lặp qua các sản phẩm và trả về danh sách ProductResponse
        return products.stream().map(product -> {
            // Lấy thông tin thương hiệu (nếu có)
            ProductBrand productBrand = product.getProductBrands().stream().findFirst().orElse(null);
            Long brandId = null;
            String brandName = null;
            if (productBrand != null) {
                brandId = productBrand.getBrand().getId();
                brandName = productBrand.getBrand().getBrandName();
            }

            // Tạo ProductResponse và trả về
            return new ProductResponse(
                    product.getId(),
                    product.getProductName(),
                    product.getColor(),
                    product.getOriginPrice(),
                    product.getSellPrice(),
                    product.getQuantity(),
                    product.getDescription(),
                    product.getSubcate() != null ? product.getSubcate().getId() : null,
                    product.getSubcate() != null ? product.getSubcate().getSubCateName() : null,
                    product.getStatus() != null ? product.getStatus().getId() : null,
                    product.getStatus() != null ? product.getStatus().getStatusName() : null,
                    brandId,    // Trả về BrandId duy nhất
                    brandName   // Trả về BrandName duy nhất
            );
        }).toList();
    }


    @Override
    public ProductResponse findProductById(Integer id) {
        // Tìm sản phẩm theo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        // Lấy thông tin thương hiệu (nếu có)
        ProductBrand productBrand = product.getProductBrands().stream().findFirst().orElse(null);
        Long brandId = null;
        String brandName = null;
        if (productBrand != null) {
            brandId = productBrand.getBrand().getId();
            brandName = productBrand.getBrand().getBrandName();
        }

        // Tạo ProductResponse và trả về
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getColor(),
                product.getOriginPrice(),
                product.getSellPrice(),
                product.getQuantity(),
                product.getDescription(),
                product.getSubcate() != null ? product.getSubcate().getId() : null,
                product.getSubcate() != null ? product.getSubcate().getSubCateName() : null,
                product.getStatus() != null ? product.getStatus().getId() : null,
                product.getStatus() != null ? product.getStatus().getStatusName() : null,
                brandId,    // Trả về BrandId
                brandName   // Trả về BrandName
        );
    }

    @Override
    public ProductResponse updateProductById(ProductRequest productRequest, Integer productId) {
        // Tìm sản phẩm theo productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Cập nhật các thuộc tính của sản phẩm từ ProductRequest
        product.setProductName(productRequest.getProductName());
        product.setColor(productRequest.getColor());
        product.setOriginPrice(productRequest.getOriginPrice());
        product.setSellPrice(productRequest.getSellPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setDescription(productRequest.getDescription());

        // Cập nhật subcategory nếu có
        if (productRequest.getSubcategoryId() != null) {
            Subcategory subcategory = subcategoryRepository.findById(productRequest.getSubcategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Subcategory not found with ID: " + productRequest.getSubcategoryId()));
            System.out.println("lỗi subcate rồi ahihi: "+subcategory);
            product.setSubcate(subcategory);
        }

        // Cập nhật status nếu có
        if (productRequest.getStatusId() != null) {
            Status status = statusRepository.findById(productRequest.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + productRequest.getStatusId()));

            System.out.println("lỗi status rồi ahihi: "+status);

            product.setStatus(status);
        }

        Product updatedProduct = productRepository.save(product);

        // Cập nhật thương hiệu nếu có
        if (productRequest.getBrandId() != null) {
            // Kiểm tra xem Brand mới có tồn tại không
            if (!brandRepository.existsById(productRequest.getBrandId())) {
                throw new EntityNotFoundException("Brand with ID " + productRequest.getBrandId() + " not found.");
            }

            // Gọi repository để cập nhật Brand cho Product
            int rowsAffected = productBrandRepository.updateBrandForProduct(
                    Long.valueOf(productRequest.getBrandId()),
                    Long.valueOf(productId)
            );

            if (rowsAffected > 0) {
                System.out.println("Successfully updated brand for product with ID: " + productId);
            } else {
                System.out.println("No records updated for product with ID: " + productId);
            }
        }

//        // Lưu lại sản phẩm đã cập nhật
//        Product updatedProduct = productRepository.save(product);

        // Lấy thông tin thương hiệu liên kết với sản phẩm
        ProductBrand productBrand = updatedProduct.getProductBrands().stream().findFirst().orElse(null);
        Long brandId = null;
        String brandName = null;
        if (productBrand != null) {
            brandId = productBrand.getBrand().getId();
            brandName = productBrand.getBrand().getBrandName();
        }

        // Trả về đối tượng ProductResponse
        return new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getProductName(),
                updatedProduct.getColor(),
                updatedProduct.getOriginPrice(),
                updatedProduct.getSellPrice(),
                updatedProduct.getQuantity(),
                updatedProduct.getDescription(),
                updatedProduct.getSubcate() != null ? updatedProduct.getSubcate().getId() : null,
                updatedProduct.getSubcate() != null ? updatedProduct.getSubcate().getSubCateName() : null,
                updatedProduct.getStatus() != null ? updatedProduct.getStatus().getId() : null,
                updatedProduct.getStatus() != null ? updatedProduct.getStatus().getStatusName() : null,
                brandId,
                brandName
        );
    }




    @Override
    public Page<ProductResponse> searchProductsPage(ProductRequest productRequest, Pageable pageable) {
        return productRepository.searchProductsPage(productRequest, pageable);
    }

    @Override
    public Page<ProductResponse> getAllProductsWithBrand(Pageable pageable) {
        return productRepository.getAllProductsWithBrand(pageable);
    }

}
