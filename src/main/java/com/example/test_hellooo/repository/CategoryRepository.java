package com.example.test_hellooo.repository;

import com.example.test_hellooo.entity.category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<category,Integer> {

}
