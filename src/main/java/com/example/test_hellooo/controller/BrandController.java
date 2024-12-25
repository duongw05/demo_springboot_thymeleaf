package com.example.test_hellooo.controller;

import com.example.test_hellooo.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class BrandController {
    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("/hien-thi/brand")
    public String brand(Model model) {
        try {
            // Lấy danh sách thương hiệu và thêm vào model
            model.addAttribute("listBrand", brandRepository.findAll());
            return "brand"; // Đảm bảo đường dẫn này là chính xác
        } catch (Exception e) {
            // Ném ngoại lệ nếu có lỗi xảy ra
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy thương hiệu", e);
        }
    }

}
