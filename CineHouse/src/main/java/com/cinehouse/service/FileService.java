package com.cinehouse.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	@Value("${file.upload.path}")
	private String uploadFolder;

	public String upload(MultipartFile poster) throws IOException {
		//1.Create file and folder to to store file if not exist
		File newFolder = new File(uploadFolder);
		if(!newFolder.exists()) {
			newFolder.mkdir();
		}
		
		//2.Get the name of file
		String originalFilename = poster.getOriginalFilename();
		
		//3.Create a path to file into given folder
		String storePath = uploadFolder+File.separator+originalFilename;
		
		//4.Copy file into created folder
		Files.copy(poster.getInputStream(), Paths.get(storePath));
		
		return originalFilename;
	}
	
	public InputStream getFile(String fileName) throws FileNotFoundException {
		//1.Create a path to get file
		String storePath = uploadFolder+File.separator+fileName;
		
		//2.Get the file using path
		return new FileInputStream(storePath);
	}
}
