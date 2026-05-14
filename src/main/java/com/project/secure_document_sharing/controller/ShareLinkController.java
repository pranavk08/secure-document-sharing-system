package com.project.secure_document_sharing.controller;

import com.project.secure_document_sharing.dto.response.SharedFilePayload;
import com.project.secure_document_sharing.service.ShareLinkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public (no JWT) download API for time-limited share tokens. Create links via
 * {@link DocumentController#createShare(Long)}.
 */
@RestController
@RequestMapping("/api/share")
@RequiredArgsConstructor
public class ShareLinkController {

	private final ShareLinkService shareLinkService;

	@GetMapping("/{token}")
	public ResponseEntity<Resource> download(@PathVariable String token, HttpServletRequest request) {
		SharedFilePayload payload = shareLinkService.loadSharedFile(token, request);
		MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
		if (payload.contentType() != null && !payload.contentType().isBlank()) {
			try {
				mediaType = MediaType.parseMediaType(payload.contentType());
			} catch (Exception ignored) {
				// keep default binary type for malformed client-supplied values
			}
		}
		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + payload.originalFilename() + "\"")
				.body(payload.resource());
	}
}
