package com.project.secure_document_sharing.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShareLinkResponse {

	private final String token;
	/** Absolute URL for the public download endpoint (no authentication). */
	private final String shareUrl;
	private final LocalDateTime expiresAt;
	private final Long documentId;
}

