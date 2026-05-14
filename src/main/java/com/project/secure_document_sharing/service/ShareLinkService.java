package com.project.secure_document_sharing.service;

import com.project.secure_document_sharing.config.ShareLinkProperties;
import com.project.secure_document_sharing.dto.response.SharedFilePayload;
import com.project.secure_document_sharing.dto.response.ShareLinkResponse;
import com.project.secure_document_sharing.entity.AccessOutcome;
import com.project.secure_document_sharing.entity.Document;
import com.project.secure_document_sharing.entity.ShareLink;
import com.project.secure_document_sharing.entity.User;
import com.project.secure_document_sharing.exception.ForbiddenException;
import com.project.secure_document_sharing.exception.ResourceNotFoundException;
import com.project.secure_document_sharing.exception.ShareLinkExpiredException;
import com.project.secure_document_sharing.mapper.ShareLinkMapper;
import com.project.secure_document_sharing.repository.DocumentRepository;
import com.project.secure_document_sharing.repository.ShareLinkRepository;
import com.project.secure_document_sharing.security.SecurityContextUser;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShareLinkService {

	private final ShareLinkRepository shareLinkRepository;
	private final DocumentRepository documentRepository;
	private final UserService userService;
	private final SecurityContextUser securityContextUser;
	private final AccessLogService accessLogService;
	private final FileStorageService fileStorageService;
	private final ShareLinkProperties shareLinkProperties;
	private final ShareLinkMapper shareLinkMapper;

	private static final String SHARE_DOWNLOAD_PATH = "/api/share/";
	private static final int SHARE_EXPIRY_MINUTES = 5;
	private static final Pattern UUID_TOKEN_PATTERN = Pattern.compile(
			"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

	@Transactional
	public ShareLinkResponse createShareLink(Long documentId) {
		User owner = userService.getByUsernameOrThrow(securityContextUser.requireUsername());
		Document document = documentRepository
				.findById(documentId)
				.orElseThrow(() -> new ResourceNotFoundException("Document not found"));
		if (!document.getOwner().getId().equals(owner.getId())) {
			throw new ForbiddenException("Not allowed to share this document");
		}
		LocalDateTime now = LocalDateTime.now();
		ShareLink link = new ShareLink();
		link.setDocument(document);
		link.setToken(UUID.randomUUID().toString());
		link.setCreatedAt(now);
		link.setExpiresAt(now.plusMinutes(SHARE_EXPIRY_MINUTES));
		shareLinkRepository.save(link);
		String shareUrl = buildShareUrl(link.getToken());
		return shareLinkMapper.toCreatedResponse(link, document.getId(), shareUrl);
	}

	@Transactional
	public SharedFilePayload loadSharedFile(String token, HttpServletRequest request) {
		validateShareTokenOrThrow(token, request);
		return shareLinkRepository
				.findByToken(token)
				.map(link -> resolveLink(link, request))
				.orElseThrow(() -> {
					accessLogService.record(null, null, null, request, AccessOutcome.NOT_FOUND);
					return new ResourceNotFoundException("Share link not found");
				});
	}

	private SharedFilePayload resolveLink(ShareLink link, HttpServletRequest request) {
		Document document = link.getDocument();
		LocalDateTime now = LocalDateTime.now();
		if (!link.getExpiresAt().isAfter(now)) {
			accessLogService.record(link, document, null, request, AccessOutcome.EXPIRED);
			throw new ShareLinkExpiredException();
		}
		if (!Files.exists(fileStorageService.resolveStoredPath(document.getStoredPath()))) {
			accessLogService.record(link, document, null, request, AccessOutcome.NOT_FOUND);
			throw new ResourceNotFoundException("Shared file no longer exists");
		}
		accessLogService.record(link, document, null, request, AccessOutcome.SUCCESS);
		Resource resource = new FileSystemResource(fileStorageService.resolveStoredPath(document.getStoredPath()));
		return new SharedFilePayload(resource, document.getOriginalFilename(), document.getContentType());
	}

	private String buildShareUrl(String token) {
		String base = shareLinkProperties.publicBaseUrl();
		if (base == null || base.isBlank()) {
			throw new IllegalStateException("app.share.public-base-url must be configured to generate share URLs");
		}
		base = base.trim();
		while (base.endsWith("/")) {
			base = base.substring(0, base.length() - 1);
		}
		return base + SHARE_DOWNLOAD_PATH + token;
	}

	private void validateShareTokenOrThrow(String token, HttpServletRequest request) {
		if (token == null || token.isBlank() || !UUID_TOKEN_PATTERN.matcher(token).matches()) {
			accessLogService.record(null, null, null, request, AccessOutcome.NOT_FOUND);
			throw new ResourceNotFoundException("Share link not found");
		}
	}
}
