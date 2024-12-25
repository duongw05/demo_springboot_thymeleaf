package com.example.test_hellooo.controller;

import com.example.test_hellooo.entity.Subcategory;
import com.example.test_hellooo.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SubcategoryController {
    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping("/hien-thi/subcategory")
    public String getAllSubcategory(Model model){
        List<Subcategory> subcategoryList = subcategoryService.getAllSubcategory();
        model.addAttribute("subcategories",subcategoryList);
        return "subcategory";
    }

}
