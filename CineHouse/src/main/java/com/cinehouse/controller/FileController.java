package com.cinehouse.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cinehouse.service.FileService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam MultipartFile poster){
		try {
			if(poster.isEmpty()) {
				throw new RuntimeException("Please provide poster");
			}
			return new ResponseEntity<>(fileService.upload(poster), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{fileName}")
	public ResponseEntity<?> getFileByName(@PathVariable String fileName, HttpServletResponse response){
		try {
			InputStream poster = fileService.getFile(fileName);
			response.setContentType(getFileContentType(fileName));
			return ResponseEntity.ok(StreamUtils.copy(poster, response.getOutputStream()));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String getFileContentType(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf("."));
		String type = null;
		switch (extension) {
		case ".jpg":
		case ".jpeg":
			type="image/jpeg";
			break;
		case ".png":
			type="image/png";
			break;
		case ".pdf":
			type="application/pdf";
			break;
		case ".txt":
			type="text/plain";
			break;
		default:
			type="text/html";
			break;
		}
		return type;
	}
}
