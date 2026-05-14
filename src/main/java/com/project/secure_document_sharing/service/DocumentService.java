package com.project.secure_document_sharing.service;

import com.project.secure_document_sharing.dto.response.DocumentResponse;
import com.project.secure_document_sharing.entity.Document;
import com.project.secure_document_sharing.entity.User;
import com.project.secure_document_sharing.exception.BadRequestException;
import com.project.secure_document_sharing.exception.ForbiddenException;
import com.project.secure_document_sharing.exception.ResourceNotFoundException;
import com.project.secure_document_sharing.mapper.DocumentMapper;
import com.project.secure_document_sharing.repository.DocumentRepository;
import com.project.secure_document_sharing.security.SecurityContextUser;
import com.project.secure_document_sharing.util.FileNameSanitizer;
import com.project.secure_document_sharing.util.UploadFilenameValidator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final DocumentRepository documentRepository;
	private final UserService userService;
	private final SecurityContextUser securityContextUser;
	private final FileStorageService fileStorageService;
	private final DocumentMapper documentMapper;

	@Transactional(readOnly = true)
	public List<DocumentResponse> listForCurrentUser() {
		User owner = userService.getByUsernameOrThrow(securityContextUser.requireUsername());
		return documentRepository.findByOwnerOrderByCreatedAtDesc(owner).stream()
				.map(documentMapper::toResponse)
				.toList();
	}

	@Transactional
	public DocumentResponse upload(MultipartFile file) throws IOException {
		validateUpload(file);
		User owner = userService.getByUsernameOrThrow(securityContextUser.requireUsername());
		String safeOriginalFilename = UploadFilenameValidator.requireSafeOriginalFilename(file.getOriginalFilename());
		String storedName = FileNameSanitizer.toStoredFileName(safeOriginalFilename);
		var target = fileStorageService.resolveStoredPath(storedName);
		Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

		Document document = documentMapper.newUploadedDocument(
				owner,
				safeOriginalFilename,
				storedName,
				file.getContentType(),
				file.getSize());
		documentRepository.save(document);
		return documentMapper.toResponse(document);
	}

	@Transactional(readOnly = true)
	public DocumentResponse getOwnedDocument(Long id) {
		User owner = userService.getByUsernameOrThrow(securityContextUser.requireUsername());
		Document document = documentRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Document not found"));
		if (!document.getOwner().getId().equals(owner.getId())) {
			throw new ForbiddenException("Not allowed to access this document");
		}
		return documentMapper.toResponse(document);
	}

	private static void validateUpload(MultipartFile file) {
		if (file == null) {
			throw new BadRequestException("File is required");
		}
		if (file.isEmpty() || file.getSize() == 0) {
			throw new BadRequestException("File is required");
		}
	}
}
