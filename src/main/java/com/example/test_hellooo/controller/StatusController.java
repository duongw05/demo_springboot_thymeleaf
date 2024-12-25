package com.example.test_hellooo.controller;

import com.example.test_hellooo.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {
    @Autowired
    StatusRepository statusRepository;

    @GetMapping("/hien-thi/status")
    public String status(Model model){
        model.addAttribute("listStatus",statusRepository.findAll());
        return "status";
    }
}
