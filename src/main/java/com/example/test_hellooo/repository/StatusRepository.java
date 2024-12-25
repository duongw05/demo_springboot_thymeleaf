package com.example.test_hellooo.repository;

import com.example.test_hellooo.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {

}
