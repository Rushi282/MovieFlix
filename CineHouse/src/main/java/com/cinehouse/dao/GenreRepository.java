package com.cinehouse.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinehouse.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

	Optional<Genre> findByName(String name);
}
