package com.cinehouse.entity;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	@NotBlank(message = "Provide movie title.")
	private String title;
	
	@Column(nullable = false)
	@NotBlank(message = "Provide movie director name.")
	private String director;
	
	@Column(nullable = false)
	@NotBlank(message = "Provide movie studio name.")
	private String studio;
	
	@Column(nullable = false)
	private Short avgVote;
	
	@Column(nullable = false, name="release_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date releaseDate;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ElementCollection
	@CollectionTable(name = "movie_cast")
	private Set<String> movieCast;
	
	@ManyToMany
	@JoinTable(
		name="movie_genre",
		joinColumns = @JoinColumn(name="movie_id"),
		inverseJoinColumns = @JoinColumn(name="genre_id")
	)
	private Set<Genre> genres;
	
	@Column(nullable = false)
	@NotBlank(message = "Provide movie poster.")
	private String poster;
}
