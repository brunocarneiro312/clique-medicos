package com.hepta.cliquemedicos.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryptor {
	public static Boolean compare(String senhaLogin, String senhaDB) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaLogin, senhaDB);
	}

	public static String encrypt(String senhaLogin) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senhaLogin);
	}
}
