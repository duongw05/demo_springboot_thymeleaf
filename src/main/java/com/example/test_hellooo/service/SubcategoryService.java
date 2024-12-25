package com.example.test_hellooo.service;

import com.example.test_hellooo.entity.Subcategory;
import com.example.test_hellooo.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService {
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<Subcategory> getAllSubcategory(){
        return subcategoryRepository.findAll();
    }

    public Subcategory getSubcategoryById(Integer id){
        return subcategoryRepository.findById(id).orElse(null);
    }

    public Subcategory saveSubcategory(Subcategory subcategory){
        return subcategoryRepository.save(subcategory);
    }

    public void deleteSubcategory(Integer id){
        subcategoryRepository.deleteById(id);
    }

    public List<Subcategory> getSubcategoriesByCategoryId(Integer categoryId) {
        return subcategoryRepository.findSubcategoriesByCategoryId(categoryId);
    }
}
