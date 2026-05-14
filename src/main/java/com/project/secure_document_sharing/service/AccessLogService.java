package com.project.secure_document_sharing.service;

import com.project.secure_document_sharing.entity.AccessLog;
import com.project.secure_document_sharing.entity.AccessOutcome;
import com.project.secure_document_sharing.entity.Document;
import com.project.secure_document_sharing.entity.ShareLink;
import com.project.secure_document_sharing.entity.User;
import com.project.secure_document_sharing.repository.AccessLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccessLogService {

	private final AccessLogRepository accessLogRepository;

	@Transactional
	public void record(
			ShareLink shareLink,
			Document document,
			User user,
			HttpServletRequest request,
			AccessOutcome outcome) {
		AccessLog log = new AccessLog();
		log.setShareLink(shareLink);
		log.setDocument(document);
		log.setUser(user);
		log.setIpAddress(request.getRemoteAddr());
		log.setUserAgent(request.getHeader("User-Agent"));
		log.setOutcome(outcome);
		log.setCreatedAt(LocalDateTime.now());
		accessLogRepository.save(log);
	}
}
