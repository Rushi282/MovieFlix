package com.cinehouse.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinehouse.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
	Optional<Category> findByName(String name);

}
