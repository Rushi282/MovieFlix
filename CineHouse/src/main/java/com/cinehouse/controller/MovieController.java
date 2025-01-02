package com.cinehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cinehouse.dto.MovieDto;
import com.cinehouse.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addMovie(@RequestPart String movieDto, @RequestPart MultipartFile poster){
		try {
			if(poster.isEmpty()) {
				throw new RuntimeException("Provide movie poster");
			}
			MovieDto newMovie = convertToMovieDto(movieDto);
			return new ResponseEntity<>(movieService.add(newMovie, poster), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/allMovies")
	public ResponseEntity<?> allMovies(){
		try {
			return ResponseEntity.ok(movieService.getMovies());
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{movieId}")
	public ResponseEntity<?> movieById(@PathVariable Integer movieId){
		try {
			return ResponseEntity.ok(movieService.getMovieById(movieId));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/title/{title}")
	public ResponseEntity<?> movieByTitle(@PathVariable String title){
		try {
			return ResponseEntity.ok(movieService.getMovieByTitle(title));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/category")
	public ResponseEntity<?> moviesByCategory(@RequestParam("category") String category){
		try {
			return ResponseEntity.ok(movieService.getMoviesByCategory(category));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update/{movieId}")
	public ResponseEntity<?> updateMovie(@PathVariable Integer movieId, @RequestPart String movieDto, @RequestPart MultipartFile poster){
		try {
			if(poster.isEmpty()) {
				poster=null;
			}
			MovieDto dto = convertToMovieDto(movieDto);
			return ResponseEntity.ok(movieService.updateMovieById(movieId, dto, poster));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{movieId}")
	public ResponseEntity<?> deleteMovie(@PathVariable Integer movieId){
		try {
			return ResponseEntity.ok(movieService.deleteMovieById(movieId));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/moviePages")
	public ResponseEntity<?> getMoviesWithPagination(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "10") Integer pageSize){
		try {
			return ResponseEntity.ok(movieService.moviesWithPagination(pageNo, pageSize));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/sortedMoviePages")
	public ResponseEntity<?> getMoviesWithPaginationAndSorting(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam String sortBy,
			@RequestParam String sortDir
			){
		try {
			return ResponseEntity.ok(movieService.moviesWithPaginationAndSorting(pageNo, pageSize, sortBy, sortDir));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private MovieDto convertToMovieDto(String dto) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(dto, MovieDto.class);
	}
}
