package com.example.test_hellooo.repository;

import com.example.test_hellooo.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory,Integer> {
    @Query("SELECT s FROM Subcategory s WHERE s.ctg.id = :categoryId")
    List<Subcategory> findSubcategoriesByCategoryId(Integer categoryId);

}
