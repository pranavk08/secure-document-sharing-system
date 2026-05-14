package com.project.secure_document_sharing.util;

import com.project.secure_document_sharing.exception.BadRequestException;
import java.util.Locale;
import java.util.Set;

public final class UploadFilenameValidator {

	public static final int MAX_ORIGINAL_FILENAME_LENGTH = 255;

	private static final Set<String> WINDOWS_RESERVED_STEMS = Set.of(
			"CON", "PRN", "AUX", "NUL",
			"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
			"LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9");

	private UploadFilenameValidator() {
	}

	/**
	 * Validates a client-supplied original filename for path traversal, control characters, reserved names,
	 * and excessive length. Returns a trimmed display name safe to persist as metadata.
	 */
	public static String requireSafeOriginalFilename(String originalFilename) {
		if (originalFilename == null || originalFilename.isBlank()) {
			throw new BadRequestException("Filename is required");
		}
		String name = originalFilename.trim();
		if (name.isEmpty()) {
			throw new BadRequestException("Filename is required");
		}
		if (name.length() > MAX_ORIGINAL_FILENAME_LENGTH) {
			throw new BadRequestException("Filename is too long");
		}
		if (name.indexOf('\0') >= 0) {
			throw new BadRequestException("Invalid filename");
		}
		if (name.chars().anyMatch(ch -> ch < 32)) {
			throw new BadRequestException("Invalid filename");
		}
		if (name.contains("..") || name.indexOf('/') >= 0 || name.indexOf('\\') >= 0) {
			throw new BadRequestException("Invalid filename");
		}
		if (isWindowsReservedFilename(name)) {
			throw new BadRequestException("Invalid filename");
		}
		return name;
	}

	private static boolean isWindowsReservedFilename(String filename) {
		int lastDot = filename.lastIndexOf('.');
		String stem = lastDot > 0 ? filename.substring(0, lastDot) : filename;
		stem = stem.replaceAll("\\s+$", "").replaceAll("\\.+$", "");
		if (stem.isEmpty()) {
			return false;
		}
		String upper = stem.toUpperCase(Locale.ROOT);
		return WINDOWS_RESERVED_STEMS.contains(upper);
	}
}
