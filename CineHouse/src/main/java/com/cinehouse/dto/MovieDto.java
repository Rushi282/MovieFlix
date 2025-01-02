package com.cinehouse.dto;

import java.util.Date;
import java.util.Set;

import com.cinehouse.entity.Category;
import com.cinehouse.entity.Genre;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {

	private Integer id;
	
	@NotBlank(message = "Provide movie title.")
	private String title;
	
	@NotBlank(message = "Provide movie director name.")
	private String director;
	
	@NotBlank(message = "Provide movie studio name.")
	private String studio;
	
	private Short avgVote;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date releaseDate;
	
	private Category category;
	
	private Set<String> movieCast;
	
	private Set<Genre> genres;
	
	@NotBlank(message = "Provide movie poster.")
	private String poster;
	
	private String posterUrl;
}
