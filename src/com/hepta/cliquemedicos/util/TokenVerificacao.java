package com.hepta.cliquemedicos.util;

import java.util.UUID;

public class TokenVerificacao {

	public static String generate() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
