package com.project.secure_document_sharing.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	private final Instant timestamp;
	private final int status;
	private final String error;
	private final String message;
	private final String path;
	/** Machine-readable code for selected errors (e.g. share link expiry). */
	private final String code;

	public ErrorResponse(int status, String error, String message, String path) {
		this(status, error, message, path, null);
	}

	public ErrorResponse(int status, String error, String message, String path, String code) {
		this.timestamp = Instant.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.code = code;
	}
}
