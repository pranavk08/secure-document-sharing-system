package com.project.secure_document_sharing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

	private final String accessToken;
	private final String tokenType;
	private final long expiresInSeconds;
}
