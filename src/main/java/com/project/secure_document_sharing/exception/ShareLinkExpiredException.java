package com.project.secure_document_sharing.exception;

import org.springframework.http.HttpStatus;

public class ShareLinkExpiredException extends ApiException {

	public static final String ERROR_CODE = "SHARE_LINK_EXPIRED";

	public ShareLinkExpiredException() {
		super(HttpStatus.GONE, "This share link has expired. Request a new share link from the document owner.");
	}

	public ShareLinkExpiredException(String message) {
		super(HttpStatus.GONE, message);
	}
}
