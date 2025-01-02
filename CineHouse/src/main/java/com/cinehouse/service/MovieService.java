package com.cinehouse.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cinehouse.dao.CategoryRepository;
import com.cinehouse.dao.GenreRepository;
import com.cinehouse.dao.MovieRepository;
import com.cinehouse.dto.MovieDto;
import com.cinehouse.dto.MoviePageDto;
import com.cinehouse.entity.Category;
import com.cinehouse.entity.Genre;
import com.cinehouse.entity.Movie;
import com.cinehouse.exception.MovieNotFoundException;

@Service
public class MovieService {

	@Autowired
	private FileService fileService;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private GenreRepository genreRepo;

	@Autowired
	private MovieRepository movieRepo;

	@Value("${file.upload.path}")
	private String uploadFolder;

	public MovieDto add(MovieDto movieDto, MultipartFile poster) throws IOException {
		// 1.upload file of respective entity.
		String storePath = uploadFolder + File.separator + poster.getOriginalFilename();
		String uploadedFile = null;
		if (Files.exists(Paths.get(storePath))) {
			throw new FileAlreadyExistsException("Given file is already exist, please provide different file");
		} else {
			uploadedFile = fileService.upload(poster);
		}

		// 2.set uploaded file name to movieDto object
		movieDto.setPoster(uploadedFile);

		// 3.get the category or save category only if it is coming first time
		Category movieCat = categoryRepo.findByName(movieDto.getCategory().getName())
				.orElseGet(() -> categoryRepo.save(movieDto.getCategory()));

		// 4.get the genres or save genres only if it is coming first time
		Set<Genre> movieGenres = movieDto.getGenres().stream()
				.map(g -> genreRepo.findByName(g.getName()).orElseGet(() -> genreRepo.save(g)))
				.collect(Collectors.toSet());

		// 5.create movie object with incoming movie DTO object
		Movie newMovie = Movie.builder().title(movieDto.getTitle()).director(movieDto.getDirector())
				.studio(movieDto.getStudio()).releaseDate(movieDto.getReleaseDate()).avgVote(movieDto.getAvgVote())
				.movieCast(movieDto.getMovieCast()).category(movieCat).genres(movieGenres).poster(movieDto.getPoster())
				.build();

		// 6. save movie
		Movie savedMovie = movieRepo.save(newMovie);

		// 7. create poster URL to get file of respective object
		String posterUrl = "http://localhost:9897/file/" + savedMovie.getPoster();

		// 8. return movieDTO response
		return MovieDto.builder().id(savedMovie.getId()).title(savedMovie.getTitle()).director(savedMovie.getDirector())
				.studio(savedMovie.getStudio()).releaseDate(savedMovie.getReleaseDate())
				.avgVote(savedMovie.getAvgVote()).movieCast(savedMovie.getMovieCast())
				.category(savedMovie.getCategory()).genres(savedMovie.getGenres()).poster(savedMovie.getPoster())
				.posterUrl(posterUrl).build();
	}

	public Collection<MovieDto> getMovies() {
		List<Movie> movies = movieRepo.findAll();
		List<MovieDto> movieDtos = movies.stream()
				.map(movie -> MovieDto.builder().id(movie.getId()).title(movie.getTitle()).director(movie.getDirector())
						.studio(movie.getStudio()).releaseDate(movie.getReleaseDate()).avgVote(movie.getAvgVote())
						.movieCast(movie.getMovieCast()).category(movie.getCategory()).genres(movie.getGenres())
						.poster(movie.getPoster()).posterUrl("http://localhost:9897/file/" + movie.getPoster()).build())
				.toList();
		return movieDtos;
	}
	
	public MovieDto getMovieById(Integer movieId) {
		Movie foundMovie = movieRepo.findById(movieId)
				.orElseThrow(() -> new MovieNotFoundException("Movie not found of id: "+movieId));
		return MovieDto.builder().id(foundMovie.getId()).title(foundMovie.getTitle()).director(foundMovie.getDirector())
				.studio(foundMovie.getStudio()).releaseDate(foundMovie.getReleaseDate())
				.avgVote(foundMovie.getAvgVote()).movieCast(foundMovie.getMovieCast())
				.category(foundMovie.getCategory()).genres(foundMovie.getGenres()).poster(foundMovie.getPoster())
				.posterUrl("http://localhost:9897/file/" + foundMovie.getPoster()).build();
	}
	
	public MovieDto getMovieByTitle(String title) {
		Movie foundMovie = movieRepo.findByTitle(title)
				.orElseThrow(() -> new MovieNotFoundException("Movie not found of title: "+title));
		return MovieDto.builder().id(foundMovie.getId()).title(foundMovie.getTitle()).director(foundMovie.getDirector())
				.studio(foundMovie.getStudio()).releaseDate(foundMovie.getReleaseDate())
				.avgVote(foundMovie.getAvgVote()).movieCast(foundMovie.getMovieCast())
				.category(foundMovie.getCategory()).genres(foundMovie.getGenres()).poster(foundMovie.getPoster())
				.posterUrl("http://localhost:9897/file/" + foundMovie.getPoster()).build();
	}
	
	public Collection<MovieDto> getMoviesByCategory(String category) {
		Category foundCat = categoryRepo.findByName(category).orElseThrow(()-> new RuntimeException("Category not found."));
		List<Movie> movies = movieRepo.findByCategory(foundCat);
		List<MovieDto> movieDtos = movies.stream()
				.map(movie -> MovieDto.builder().id(movie.getId()).title(movie.getTitle()).director(movie.getDirector())
						.studio(movie.getStudio()).releaseDate(movie.getReleaseDate()).avgVote(movie.getAvgVote())
						.movieCast(movie.getMovieCast()).category(movie.getCategory()).genres(movie.getGenres())
						.poster(movie.getPoster()).posterUrl("http://localhost:9897/file/" + movie.getPoster()).build())
				.toList();
		return movieDtos;
	}
	
	public MovieDto updateMovieById(Integer movieId, MovieDto updatedMovieDto, MultipartFile newPoster) throws IOException {
		Movie foundMovie = movieRepo.findById(movieId)
				.orElseThrow(() -> new MovieNotFoundException("Movie not found of id: "+movieId));
		if(newPoster != null) {
			String existingPoster = foundMovie.getPoster();
			String storePath = uploadFolder+File.separator+existingPoster;
			boolean isDeleted = Files.deleteIfExists(Paths.get(storePath));
			if(isDeleted) {
				System.out.println("is deleted block");
				String newUploadedPoster = fileService.upload(newPoster);
				updatedMovieDto.setPoster(newUploadedPoster);
			}
		}else {
			updatedMovieDto.setPoster(foundMovie.getPoster());
		}
		
		Category newCat = categoryRepo.findByName(updatedMovieDto.getCategory().getName())
		.orElseGet(() -> categoryRepo.save(updatedMovieDto.getCategory()));
		
		Set<Genre> newGenres = updatedMovieDto.getGenres().stream()
		.map(g -> genreRepo.findByName(g.getName()).orElseGet(()->genreRepo.save(g)))
		.collect(Collectors.toSet());
		
		foundMovie.setTitle(updatedMovieDto.getTitle());
		foundMovie.setDirector(updatedMovieDto.getDirector());
		foundMovie.setStudio(updatedMovieDto.getStudio());
		foundMovie.setMovieCast(updatedMovieDto.getMovieCast());
		foundMovie.setReleaseDate(updatedMovieDto.getReleaseDate());
		foundMovie.setAvgVote(updatedMovieDto.getAvgVote());
		foundMovie.setGenres(newGenres);
		foundMovie.setCategory(newCat);
		foundMovie.setPoster(updatedMovieDto.getPoster());
		
		Movie updatedMovie = movieRepo.save(foundMovie);
		
		return MovieDto.builder().id(updatedMovie.getId()).title(updatedMovie.getTitle()).director(updatedMovie.getDirector())
				.studio(updatedMovie.getStudio()).releaseDate(updatedMovie.getReleaseDate())
				.avgVote(updatedMovie.getAvgVote()).movieCast(updatedMovie.getMovieCast())
				.category(updatedMovie.getCategory()).genres(updatedMovie.getGenres()).poster(updatedMovie.getPoster())
				.posterUrl("http://localhost:9897/file/"+updatedMovie.getPoster()).build();
	}
	
	public String deleteMovieById(Integer movieId) {
		Movie foundMovie = movieRepo.findById(movieId)
				.orElseThrow(() -> new MovieNotFoundException("Movie not found of id: "+movieId));
		movieRepo.delete(foundMovie);
		return "Movie deleted of id: "+movieId;
	}
	
	public MoviePageDto moviesWithPagination(Integer pageNo, Integer pageSize) {
		
		//1. Create a page 
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		//2. get movies on page 
		Page<Movie> moviePage = movieRepo.findAll(pageable);
		
		int currentPageNumber = moviePage.getNumber();
		int totalPages = moviePage.getTotalPages();
		long totalElements = moviePage.getTotalElements();
		boolean isFirst = moviePage.isFirst();
		boolean isLast = moviePage.isLast();
		List<Movie> movies = moviePage.getContent();
		
		List<MovieDto> movieDtos = movies.stream().map(movie -> MovieDto.builder()
				.id(movie.getId())
				.title(movie.getTitle())
				.director(movie.getDirector())
				.studio(movie.getStudio())
				.releaseDate(movie.getReleaseDate())
				.avgVote(movie.getAvgVote())
				.movieCast(movie.getMovieCast())
				.category(movie.getCategory())
				.genres(movie.getGenres())
				.poster(movie.getPoster())
				.posterUrl("http://localhost:3306/file/"+movie.getPoster())
				.build()
			).toList();
		
		return MoviePageDto.builder()
				.currentPageNumber(currentPageNumber)
				.isFirst(isFirst)
				.isLast(isLast)
				.movieDtos(movieDtos)
				.totalElements(totalElements)
				.totalPages(totalPages)
				.build();
	}

	public MoviePageDto moviesWithPaginationAndSorting(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Movie> moviePage = movieRepo.findAll(pageable);
		
		int currentPageNumber = moviePage.getNumber();
		int totalPages = moviePage.getTotalPages();
		long totalElements = moviePage.getTotalElements();
		boolean isFirst = moviePage.isFirst();
		boolean isLast = moviePage.isLast();
		List<Movie> movies = moviePage.getContent();
		
		List<MovieDto> movieDtos = movies.stream().map(movie -> MovieDto.builder()
				.id(movie.getId())
				.title(movie.getTitle())
				.director(movie.getDirector())
				.studio(movie.getStudio())
				.releaseDate(movie.getReleaseDate())
				.avgVote(movie.getAvgVote())
				.movieCast(movie.getMovieCast())
				.category(movie.getCategory())
				.genres(movie.getGenres())
				.poster(movie.getPoster())
				.posterUrl("http://localhost:3306/file/"+movie.getPoster())
				.build()
			).toList();
		
		return MoviePageDto.builder()
				.currentPageNumber(currentPageNumber)
				.isFirst(isFirst)
				.isLast(isLast)
				.movieDtos(movieDtos)
				.totalElements(totalElements)
				.totalPages(totalPages)
				.build();
	}
	
}
