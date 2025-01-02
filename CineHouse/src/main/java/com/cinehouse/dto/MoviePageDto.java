package com.cinehouse.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MoviePageDto {

	private int currentPageNumber;
	private int totalPages;
	private long totalElements;
	private boolean isFirst;
	private boolean isLast;
	private List<MovieDto> movieDtos;
}
