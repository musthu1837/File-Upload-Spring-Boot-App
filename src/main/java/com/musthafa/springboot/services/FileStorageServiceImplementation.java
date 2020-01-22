package com.musthafa.springboot.services;

import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.musthafa.springboot.exceptions.FileNotFountException;
import com.musthafa.springboot.exceptions.FileStorageException;
import com.musthafa.springboot.fileconfiguration.FileStorageProperties;
import com.musthafa.springboot.models.File;
import com.musthafa.springboot.repositories.FileRepository;

@Service
public class FileStorageServiceImplementation implements FileStorageService {

	private Path fileStorageLocation;
	@Autowired
	private FileRepository fileRepository;

	@Autowired
	public void FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	@Override
	public File uploadFile(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		File fileEntity = new File();
		fileEntity.setFileName(file.getOriginalFilename());
		fileEntity.setFileType(file.getOriginalFilename().split("\\.")[1]);
		fileEntity.setSize(file.getSize());
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.contains("..")) {
			throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
		}

		Path targetLocation = this.fileStorageLocation.resolve(fileName);
		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		fileRepository.save(fileEntity);
		return fileEntity;
	}

	@Override
	public Resource getFileById(int fileId) {
		// TODO Auto-generated method stub
		Resource resource = null;
		Optional<File> file = fileRepository.findById(fileId);
		if (file.isPresent()) {
			Path filePath = this.fileStorageLocation.resolve(file.get().getFileName()).normalize();
			try {
				resource = new UrlResource(filePath.toUri());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resource;
		} else {
			throw new FileNotFountException("file Id" + fileId);
		}

	}

	@Override
	public void deleteFileById(int fileId) throws IOException {
		// TODO Auto-generated method stub
		Optional<File> file = fileRepository.findById(fileId);
		if (file.isPresent()) {
			Path filePath = this.fileStorageLocation.resolve(file.get().getFileName()).normalize();
			Files.delete(filePath);
			fileRepository.deleteById(fileId);
			
		} else {
			throw new FileNotFountException("file Id" + fileId);
		}		
	}

}
