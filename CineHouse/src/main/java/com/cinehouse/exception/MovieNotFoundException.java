package com.cinehouse.exception;

public class MovieNotFoundException extends RuntimeException{

	public MovieNotFoundException(String msg) {
		super(msg);
	}
}
