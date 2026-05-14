package com.project.secure_document_sharing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

	@NotBlank
	@Size(max = 128)
	private String username;

	@NotBlank
	@Size(min = 8, max = 128)
	private String password;
}
