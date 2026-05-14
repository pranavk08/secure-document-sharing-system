package com.project.secure_document_sharing.mapper;

import com.project.secure_document_sharing.dto.response.DocumentResponse;
import com.project.secure_document_sharing.entity.Document;
import com.project.secure_document_sharing.entity.User;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

	public Document newUploadedDocument(
			User owner,
			String safeOriginalFilename,
			String storedPath,
			String contentType,
			long sizeBytes) {
		Document document = new Document();
		document.setOwner(owner);
		document.setOriginalFilename(safeOriginalFilename);
		document.setStoredPath(storedPath);
		document.setContentType(contentType);
		document.setSizeBytes(sizeBytes);
		document.setCreatedAt(LocalDateTime.now());
		return document;
	}

	public DocumentResponse toResponse(Document document) {
		return new DocumentResponse(
				document.getId(),
				document.getOriginalFilename(),
				document.getContentType(),
				document.getSizeBytes(),
				document.getCreatedAt());
	}
}
