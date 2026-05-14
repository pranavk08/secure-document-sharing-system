package com.project.secure_document_sharing.dto.response;

import org.springframework.core.io.Resource;

public record SharedFilePayload(Resource resource, String originalFilename, String contentType) {
}
