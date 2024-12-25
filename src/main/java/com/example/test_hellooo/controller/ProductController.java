package com.example.test_hellooo.controller;

import com.example.test_hellooo.entity.Brand;
import com.example.test_hellooo.entity.Status;
import com.example.test_hellooo.entity.Subcategory;
import com.example.test_hellooo.repository.BrandRepository;
import com.example.test_hellooo.repository.StatusRepository;
import com.example.test_hellooo.request.ProductRequest;
import com.example.test_hellooo.respone.ProductResponse;
import com.example.test_hellooo.service.ProductBrandService;
import com.example.test_hellooo.service.ProductService;
import com.example.test_hellooo.service.SubcategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hien-thi/products")
public class ProductController {
    private final ProductService productService;

    private final SubcategoryService subcategoryService;

    private final ProductBrandService productBrandService;

    private final StatusRepository statusRepository;

    private final BrandRepository brandRepository;

    @Autowired
    public ProductController(ProductService productService, BrandRepository brandRepository, SubcategoryService subcategoryService, ProductBrandService productBrandService, StatusRepository statusRepository) {
        this.productService = productService;
        this.brandRepository = brandRepository;
        this.subcategoryService = subcategoryService;
        this.productBrandService = productBrandService;
        this.statusRepository = statusRepository;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String getAllProductsOrSearch(@ModelAttribute ProductRequest request, Model model) {
        // Lấy danh sách cho combobox
        List<Brand> listBrand = brandRepository.findAll();
        List<Subcategory> subcategories = subcategoryService.getAllSubcategory();
        List<Status> listStatus = statusRepository.findAll();

        // Truyền danh sách vào model cho combobox
        model.addAttribute("listBrand", listBrand);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("listStatus", listStatus);

        // Phân trang
        int page = (request.getPage() != null) ? request.getPage() : 0;
        int size = (request.getSize() != null) ? request.getSize() : 5;
        Pageable pageable = PageRequest.of(page, size);

        // Tạo đối tượng Page để chứa kết quả tìm kiếm
        Page<ProductResponse> productPage;

        System.out.println("productname oooo: "+request.getProductName());
        System.out.println("price oooo: "+request.getSellPrice());
        System.out.println("brand oooo: "+request.getBrandId());
        System.out.println("subcate oooo: "+request.getSubcategoryId());
        System.out.println("status oooo: "+request.getStatusId());
        System.out.println("\n");

        // Kiểm tra nếu các tham số tìm kiếm không rỗng hoặc null
        if (request.getProductName() != null || request.getSellPrice() != null ||
                request.getBrandId() != null || request.getSubcategoryId() != null ||
                request.getStatusId() != null) {

            // Tìm kiếm sản phẩm với các tham số từ ProductRequest
            productPage = productService.searchProductsPage(request, pageable);
        } else {
            // Nếu không có tìm kiếm, lấy tất cả sản phẩm
            productPage = productService.getAllProductsWithBrand(pageable);
        }

        // Tạo đối tượng Response và truyền vào model
        model.addAttribute("products", productPage.getContent()); // Truyền danh sách riêng
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalEntries", productPage.getTotalElements());

        model.addAttribute("offset", page * size); // Cung cấp offset cho view nếu cần

        return "product"; // Trả về view "product"
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest());

        List<Brand> listBrand = brandRepository.findAll();
        List<Subcategory> subcategories = subcategoryService.getAllSubcategory();
        List<Status> listStatus = statusRepository.findAll();

        model.addAttribute("listBrand", listBrand);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("listStatus", listStatus);

        return "addproduct"; // Trang sẽ được hiển thị khi người dùng truy cập '/hien-thi/products/add'
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("productRequest") ProductRequest productRequest,
                             BindingResult result,
                             Model model) {

        // Nếu có lỗi validation, quay lại trang form với thông báo lỗi
        if (result.hasErrors()) {
            List<Brand> listBrand = brandRepository.findAll();
            List<Subcategory> subcategories = subcategoryService.getAllSubcategory();
            List<Status> listStatus = statusRepository.findAll();

            model.addAttribute("listBrand", listBrand);
            model.addAttribute("subcategories", subcategories);
            model.addAttribute("listStatus", listStatus);
            return "addproduct";
        }

        // Ánh xạ từ ProductRequest sang Product entity
        ProductRequest product = new ProductRequest();

        product.setProductName(productRequest.getProductName().trim());
        product.setColor(productRequest.getColor());
        product.setOriginPrice(productRequest.getOriginPrice());
        product.setSellPrice(productRequest.getSellPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setDescription(productRequest.getDescription());

        // Lấy thông tin từ ID trong ProductRequest và ánh xạ vào entity
        Subcategory subcategory = subcategoryService.getSubcategoryById(productRequest.getSubcategoryId());
        Status status = statusRepository.getById(productRequest.getStatusId());
        Brand brands = brandRepository.getById(productRequest.getBrandId());

        // Gán danh sách thương hiệu vào đối tượng Product
        product.setBrandId(productRequest.getBrandId());

        // Gán các đối tượng Subcategory và Status vào product
        product.setSubcategoryId(productRequest.getSubcategoryId());
        product.setStatusId(productRequest.getStatusId());


        // Lưu vào cơ sở dữ liệu
        productService.saveProduct(product);

        // Chuyển hướng tới trang danh sách sản phẩm sau khi thêm thành công
        return "redirect:/hien-thi/products";
    }


    @GetMapping("/detail/{id}")
    public String showdetailProductForm(@PathVariable("id") Integer productId, Model model) {

        System.out.println("product id: " + productId);
        // Lấy sản phẩm theo ID
        ProductResponse product = productService.findProductById(Integer.valueOf(productId));

        // Thêm sản phẩm vào model
        model.addAttribute("product", product);

        // Lấy danh sách brand, subcategory và status để truyền vào model
        List<Brand> brands = brandRepository.findAll();
        List<Subcategory> subcategories = subcategoryService.getAllSubcategory();
        List<Status> statuses = statusRepository.findAll();

        model.addAttribute("brandList", brands);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("statuses", statuses);

        return "detailproduct"; // Trả về trang JSP chi tiết sản phẩm
    }

    @GetMapping("/update/{id}")
    public String showUpdateProduct(@PathVariable("id") Integer productId, Model model) {
        // Lấy dữ liệu sản phẩm và truyền vào model
        ProductResponse productResponse = productService.findProductById(productId);

        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(productResponse.getId());
        productRequest.setProductName(productResponse.getProductName());
        productRequest.setColor(productResponse.getColor());
        productRequest.setQuantity(productResponse.getQuantity());
        productRequest.setSellPrice(productResponse.getSellPrice());
        productRequest.setOriginPrice(productResponse.getOriginPrice());
        productRequest.setBrandId(productResponse.getBrandId().intValue());
        productRequest.setSubcategoryId(productResponse.getSubcategoryId());
        productRequest.setStatusId(productResponse.getStatusId());

        model.addAttribute("product", productRequest);

        // Lấy danh sách brand, subcategory và status để truyền vào model
        List<Brand> brands = brandRepository.findAll();
        List<Subcategory> subcategories = subcategoryService.getAllSubcategory();
        List<Status> statuses = statusRepository.findAll();

        model.addAttribute("brandList", brands);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("statuses", statuses);

        return "updateproduct"; // Chuyển đến trang updateproduct
    }

    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable("id") Integer productId,
            @ModelAttribute("product") @Valid ProductRequest productRequest,
            BindingResult bindingResult,
            Model model) {

        System.out.println("productBrand: " + productRequest.getBrandId());
        System.out.println("productName: " + productRequest.getProductName());
        System.out.println("color: " + productRequest.getColor());
        System.out.println("quantity: " + productRequest.getQuantity());
        System.out.println("price: " + productRequest.getSellPrice());
        System.out.println("status: " + productRequest.getStatusId());
        System.out.println("subcate: " + productRequest.getSubcategoryId());
        System.out.println("originprice: " + productRequest.getOriginPrice());

        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productRequest);
            model.addAttribute("brandList", brandRepository.findAll());
            model.addAttribute("subcategories", subcategoryService.getAllSubcategory());
            model.addAttribute("statuses", statusRepository.findAll());
            model.addAttribute("errorMessage", "Validation failed! Please check the form.");
            return "updateproduct"; // Tên view để hiển thị lại form
        }

        try {
            // Gọi service để cập nhật sản phẩm
            ProductResponse updatedProduct = productService.updateProductById(productRequest, productId);

            // Thêm thông báo thành công
            model.addAttribute("successMessage", "Product updated successfully: " + updatedProduct.getProductName());
        } catch (EntityNotFoundException e) {
            // Thêm thông báo lỗi nếu không tìm thấy sản phẩm
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác và thêm thông báo lỗi
            model.addAttribute("errorMessage", "Error updating product: " + e.getMessage());
        }

        // Cập nhật lại dữ liệu để hiển thị trên form
        model.addAttribute("brandList", brandRepository.findAll());
        model.addAttribute("subcategories", subcategoryService.getAllSubcategory());
        model.addAttribute("statuses", statusRepository.findAll());
        return "redirect:/hien-thi/products";
    }


}
