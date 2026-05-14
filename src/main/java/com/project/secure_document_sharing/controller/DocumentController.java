package com.project.secure_document_sharing.controller;

import com.project.secure_document_sharing.dto.response.DocumentResponse;
import com.project.secure_document_sharing.dto.response.ShareLinkResponse;
import com.project.secure_document_sharing.service.DocumentService;
import com.project.secure_document_sharing.service.ShareLinkService;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

	private final DocumentService documentService;
	private final ShareLinkService shareLinkService;

	/**
	 * Multipart upload for the authenticated user. Requires an {@code Authorization} header with a Bearer JWT.
	 * Bytes are written under {@code app.storage.root-directory}; metadata is persisted to MySQL.
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DocumentResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(documentService.upload(file));
	}

	@GetMapping
	public ResponseEntity<List<DocumentResponse>> list() {
		return ResponseEntity.ok(documentService.listForCurrentUser());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DocumentResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(documentService.getOwnedDocument(id));
	}

	/**
	 * Creates a 5-minute temporary share link for this document. Requires Bearer JWT. Response includes full
	 * {@code shareUrl} and {@code Location} header for anonymous {@code GET /api/share/{token}} download.
	 */
	@PostMapping("/{id}/share")
	public ResponseEntity<ShareLinkResponse> createShare(@PathVariable Long id) {
		ShareLinkResponse body = shareLinkService.createShareLink(id);
		return ResponseEntity.created(URI.create(body.getShareUrl())).body(body);
	}
}
