package com.project.secure_document_sharing.repository;

import com.project.secure_document_sharing.entity.Document;
import com.project.secure_document_sharing.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

	List<Document> findByOwnerOrderByCreatedAtDesc(User owner);
}
