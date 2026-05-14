package com.project.secure_document_sharing.service;

import com.project.secure_document_sharing.dto.request.LoginRequest;
import com.project.secure_document_sharing.dto.request.RegisterRequest;
import com.project.secure_document_sharing.dto.response.AuthResponse;
import com.project.secure_document_sharing.entity.User;
import com.project.secure_document_sharing.exception.ConflictException;
import com.project.secure_document_sharing.repository.UserRepository;
import com.project.secure_document_sharing.security.JwtTokenProvider;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public void register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new ConflictException("Username already taken");
		}
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		String token = jwtTokenProvider.generateToken(authentication);
		long expiresInSeconds = Duration.ofMinutes(jwtTokenProvider.getExpirationMinutes()).toSeconds();
		return new AuthResponse(token, "Bearer", expiresInSeconds);
	}
}
