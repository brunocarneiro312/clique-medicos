package com.hepta.cliquemedicos.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidadorCPFTest {
	private List<String> cpfsValidos;
	private List<String> cpfsInvalidos;

	@BeforeEach
	void setUp() throws Exception {
		cpfsValidos = new ArrayList<>();
		cpfsInvalidos = new ArrayList<>();
		
		cpfsValidos.add("983.334.927-70");
		cpfsValidos.add("98333492770");
		cpfsValidos.add("950.049.878-23");
		cpfsValidos.add("95004987823");
		cpfsValidos.add("463.710.288-04");
		cpfsValidos.add("46371028804");
		cpfsValidos.add("332.982.841-22");
		cpfsValidos.add("33298284122");
		cpfsValidos.add("904.320.117-06");
		cpfsValidos.add("90432011706");
		cpfsValidos.add("953.056.814-21");
		cpfsValidos.add("95305681421");
		cpfsValidos.add("961.537.444-00");
		cpfsValidos.add("96153744400");
		cpfsValidos.add("161.038.720-14");
		cpfsValidos.add("16103872014");
		cpfsValidos.add("131.351.885-90");
		cpfsValidos.add("13135188590");
		cpfsValidos.add("229.375.756-09");
		cpfsValidos.add("22937575609");
		cpfsValidos.add("203.013.269-16");
		cpfsValidos.add("20301326916");
		cpfsValidos.add("557.730.192-05");
		cpfsValidos.add("55773019205");
		cpfsValidos.add("250.303.501-95");
		cpfsValidos.add("25030350195");
		cpfsValidos.add("184.090.365-17");
		cpfsValidos.add("18409036517");
		cpfsValidos.add("166.767.120-08");
		cpfsValidos.add("16676712008");
		cpfsValidos.add("249.900.866-00");
		cpfsValidos.add("24990086600");
		cpfsValidos.add("042.800.952-21");
		cpfsValidos.add("04280095221");
		cpfsValidos.add("117.399.520-06");
		cpfsValidos.add("11739952006");
		cpfsValidos.add("628.054.207-64");
		cpfsValidos.add("62805420764");
		cpfsValidos.add("367.638.574-89");
		cpfsValidos.add("36763857489");
		cpfsValidos.add("852.442.056-17");
		cpfsValidos.add("85244205617");
		cpfsValidos.add("758.739.604-70");
		cpfsValidos.add("75873960470");
		cpfsValidos.add("773.145.375-85");
		cpfsValidos.add("77314537585");
		cpfsValidos.add("978.165.675-10");
		cpfsValidos.add("97816567510");
		cpfsValidos.add("227.038.899-25");
		cpfsValidos.add("22703889925");
		cpfsValidos.add("910.370.118-21");
		cpfsValidos.add("91037011821");
		cpfsValidos.add("570.502.544-01");
		cpfsValidos.add("57050254401");
		cpfsValidos.add("877.236.032-15");
		cpfsValidos.add("87723603215");
		cpfsValidos.add("413.926.628-77");
		cpfsValidos.add("41392662877");
		cpfsValidos.add("521.235.687-32");
		cpfsValidos.add("52123568732");

		cpfsInvalidos.add("a");
		cpfsInvalidos.add("");
		cpfsInvalidos.add("vwucm8wmf7dsf6y98734");
		cpfsInvalidos.add("aaaaaaaaaaa");
		cpfsInvalidos.add("00000000000");
		cpfsInvalidos.add("11111111111");
		cpfsInvalidos.add("22222222222");
		cpfsInvalidos.add("33333333333");
		cpfsInvalidos.add("44444444444");
		cpfsInvalidos.add("55555555555");
		cpfsInvalidos.add("66666666666");
		cpfsInvalidos.add("77777777777");
		cpfsInvalidos.add("88888888888");
		cpfsInvalidos.add("99999999999");
		cpfsInvalidos.add("!@#$%¨&*()_-+=");
		cpfsInvalidos.add("!@#$%¨&*()_");
	}

	@Test
	void testValidar() {
		for (String s : cpfsInvalidos) {
			if (ValidadorCPF.validar(s))
				fail("CPF inválido foi visto como válido: " + s);
		}
		for (String s : cpfsValidos) {
			if (!ValidadorCPF.validar(s))
				fail("CPF válido foi visto como inválido: " + s);
		}
		assert (true);
	}

}
