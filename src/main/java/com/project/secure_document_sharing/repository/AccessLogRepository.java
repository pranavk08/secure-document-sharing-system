package com.project.secure_document_sharing.repository;

import com.project.secure_document_sharing.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
