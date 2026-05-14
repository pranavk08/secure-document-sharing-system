package com.project.secure_document_sharing.util;

import java.util.UUID;

public final class FileNameSanitizer {

	private FileNameSanitizer() {
	}

	/**
	 * Produces a filesystem-safe stored name while avoiding path traversal from user-supplied filenames.
	 */
	public static String toStoredFileName(String originalFilename) {
		String suffix = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
			if (suffix.length() > 16) {
				suffix = "";
			}
		}
		return UUID.randomUUID() + suffix.replaceAll("[^a-zA-Z0-9.]", "");
	}
}
