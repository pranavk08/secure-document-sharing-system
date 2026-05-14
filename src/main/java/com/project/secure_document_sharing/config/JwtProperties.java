package com.project.secure_document_sharing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String issuer, String secret, long expirationMinutes) {
}
