package com.project.secure_document_sharing.repository;

import com.project.secure_document_sharing.entity.ShareLink;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {

	Optional<ShareLink> findByToken(String token);
}
