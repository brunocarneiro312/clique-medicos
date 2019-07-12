package com.hepta.cliquemedicos.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EncryptorTest {
	private String senhaTeste = "";
	@BeforeEach
	void setUp() throws Exception {
		senhaTeste = "rodavermelha";
	}

	@Test
	void testEncrypt() {
		String senhaEcryptada = Encryptor.encrypt(senhaTeste);
		System.out.println(senhaEcryptada);
		if(senhaEcryptada.equals(senhaTeste))
			fail("Não encriptou.");
		System.out.println(senhaEcryptada);
		if(Encryptor.compare(senhaTeste, senhaEcryptada))
			assert(true);
	}

}
