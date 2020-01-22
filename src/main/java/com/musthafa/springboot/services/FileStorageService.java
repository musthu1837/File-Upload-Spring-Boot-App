package com.musthafa.springboot.services;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.musthafa.springboot.models.File;

public interface FileStorageService {
	public File uploadFile(MultipartFile file) throws IOException;
	public Resource getFileById(int fileId);
	public void deleteFileById(int fileId) throws IOException;
}
