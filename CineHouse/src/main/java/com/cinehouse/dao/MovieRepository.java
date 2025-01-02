package com.cinehouse.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinehouse.entity.Category;
import com.cinehouse.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

	Optional<Movie> findByTitle(String title);
	List<Movie> findByCategory(Category category);
}
