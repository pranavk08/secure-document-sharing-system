package com.project.secure_document_sharing.exception;

import com.project.secure_document_sharing.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ShareLinkExpiredException.class)
	public ResponseEntity<ErrorResponse> handleShareLinkExpired(
			ShareLinkExpiredException ex, HttpServletRequest request) {
		ErrorResponse body = new ErrorResponse(
				ex.getStatus().value(),
				ex.getStatus().getReasonPhrase(),
				ex.getMessage(),
				request.getRequestURI(),
				ShareLinkExpiredException.ERROR_CODE);
		return ResponseEntity.status(ex.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(body);
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
		ErrorResponse body = new ErrorResponse(
				ex.getStatus().value(),
				ex.getStatus().getReasonPhrase(),
				ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(ex.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(
			MethodArgumentNotValidException ex, HttpServletRequest request) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		ErrorResponse body = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				message,
				request.getRequestURI());
		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(body);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentials(
			BadCredentialsException ex, HttpServletRequest request) {
		ErrorResponse body = new ErrorResponse(
				HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase(),
				"Invalid credentials",
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(body);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxUploadSize(
			MaxUploadSizeExceededException ex, HttpServletRequest request) {
		ErrorResponse body = new ErrorResponse(
				HttpStatus.PAYLOAD_TOO_LARGE.value(),
				HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase(),
				"Uploaded file exceeds the configured maximum size",
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).contentType(MediaType.APPLICATION_JSON).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
		ErrorResponse body = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
				"An unexpected error occurred",
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(body);
	}
}
