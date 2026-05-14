package com.project.secure_document_sharing.mapper;

import com.project.secure_document_sharing.dto.response.ShareLinkResponse;
import com.project.secure_document_sharing.entity.ShareLink;
import org.springframework.stereotype.Component;

@Component
public class ShareLinkMapper {

	public ShareLinkResponse toCreatedResponse(ShareLink link, Long documentId, String shareUrl) {
		return new ShareLinkResponse(link.getToken(), shareUrl, link.getExpiresAt(), documentId);
	}
}
