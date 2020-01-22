package com.musthafa.springboot.controllers;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.musthafa.springboot.models.File;
import com.musthafa.springboot.services.FileStorageService;

@RestController
public class FileController {
	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("/v1/files")
	public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		File fileEntity = fileStorageService.uploadFile(file);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{fileId}")
				.buildAndExpand(fileEntity.getFileId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/v1/files/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int fileId, HttpServletRequest request)
			throws IOException {
		// Load file as Resource
		Resource resource = fileStorageService.getFileById(fileId);

		String contentType = null;
		contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@DeleteMapping("/v1/files/{fileId}")
	public ResponseEntity<Object> deleteFile(@PathVariable int fileId) throws IOException {
		fileStorageService.deleteFileById(fileId);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}