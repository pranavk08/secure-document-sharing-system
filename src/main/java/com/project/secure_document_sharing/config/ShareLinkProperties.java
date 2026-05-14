package com.project.secure_document_sharing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.share")
public record ShareLinkProperties(
		/**
		 * Public origin used when returning full share download URLs (no trailing slash), e.g.
		 * {@code https://api.example.com} or {@code http://localhost:8080}.
		 */
		String publicBaseUrl) {
}
