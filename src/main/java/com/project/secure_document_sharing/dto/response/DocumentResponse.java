package com.project.secure_document_sharing.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentResponse {

	private final Long id;
	private final String originalFilename;
	private final String contentType;
	private final Long sizeBytes;
	private final LocalDateTime createdAt;
}
