package com.project.secure_document_sharing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.project.secure_document_sharing.config.FileStorageProperties;
import com.project.secure_document_sharing.config.JwtProperties;
import com.project.secure_document_sharing.config.ShareLinkProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, FileStorageProperties.class, ShareLinkProperties.class})
public class SecureDocumentSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureDocumentSharingApplication.class, args);
	}
}
