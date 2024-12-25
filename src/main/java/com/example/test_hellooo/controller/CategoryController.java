package com.example.test_hellooo.controller;

import com.example.test_hellooo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {
    @Autowired
    CategoryRepository repo;
    @GetMapping("/hien-thi/category")
    public String index(Model model){
        model.addAttribute("list", repo.findAll());
        return "index";
    }
}
