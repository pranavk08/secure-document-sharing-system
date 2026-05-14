package com.project.secure_document_sharing.service;

import com.project.secure_document_sharing.config.FileStorageProperties;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileStorageService {

	private final FileStorageProperties properties;

	@PostConstruct
	public void ensureStorageReady() throws IOException {
		Path root = Path.of(properties.rootDirectory());
		Files.createDirectories(root);
	}

	public Path resolveStoredPath(String storedFileName) {
		Path root = Path.of(properties.rootDirectory()).toAbsolutePath().normalize();
		Path resolved = root.resolve(storedFileName).normalize();
		if (!resolved.startsWith(root)) {
			throw new IllegalStateException("Invalid stored path");
		}
		return resolved;
	}
}
