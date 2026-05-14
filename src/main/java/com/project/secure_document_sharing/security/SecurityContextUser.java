package com.project.secure_document_sharing.security;

import com.project.secure_document_sharing.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUser {

	public String requireUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizedException("Authentication required");
		}
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails)) {
			throw new UnauthorizedException("Authentication required");
		}
		return userDetails.getUsername();
	}
}
