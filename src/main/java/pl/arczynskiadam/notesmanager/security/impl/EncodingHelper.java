package pl.arczynskiadam.notesmanager.security.impl;

import pl.arczynskiadam.notesmanager.web.SecurityConstants;

public class EncodingHelper {

	public static String buildPlainText(String plainPassword, String salt) {
		return salt.concat(SecurityConstants.DELIMITER).concat(plainPassword);
	}

}
