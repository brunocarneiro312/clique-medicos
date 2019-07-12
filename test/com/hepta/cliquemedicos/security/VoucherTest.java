package com.hepta.cliquemedicos.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoucherTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGerar() {
		for (int i = 0; i < 100000; i++) {
			Long test = Voucher.gerar();
			if(test < Voucher.MIN)
				fail("Erro: menor que minimo");
			else if(test > Voucher.MAX)
				fail("Erro: maior que máximo");
		}
		assert(true);
	}
	
	@Test
	void testGerarString() {
		for (int i = 0; i < 100000; i++) {
			String test = Voucher.gerarString();
			if(test.length() == 0)
				fail("Erro: menor que minimo");
			else if(test.length() > 10)
				fail("Erro: maior que máximo");
		}
		assert(true);
	}

}
