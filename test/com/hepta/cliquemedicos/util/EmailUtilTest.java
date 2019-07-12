package com.hepta.cliquemedicos.util;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hepta.cliquemedicos.dto.CamposEmailDTO;
import com.hepta.cliquemedicos.dto.enums.TipoEmailEnum;

class EmailUtilTest {
	CamposEmailDTO campos;

	@BeforeEach
	void setUp() throws Exception {
		campos = new CamposEmailDTO();
		campos.setMatriculaBeneficiario("1");
		campos.setCredenciado("1");
		campos.setDataCompra("2019-05-24T07:00:00.435");
		campos.setEnderecoAtendimento("SHLS QD 716 BLOCO E SALA 401 CENTRO MÉDICO DE BRASÍLIA");
		campos.setEspecialidade("Clínica Médica");
		campos.setEspecialidadeDescricao("Descrição especialidade teste");
		campos.setInicio("2019-05-24T07:00:00.435");
		campos.setFim("2019-05-24T07:10:00.435");
		campos.setIdAgendamento(1);
		campos.setNomeBeneficiario("Nome beneficiario teste");
		campos.setNomeCredenciado("Nome credenciado teste");
		campos.setNumeroVoucher("NUMERO VOUCHER TESTE");
		campos.setAssociadoEndereco("Endereço beneficiario teste");
		campos.setPrestador("Nome prestador teste");
		campos.setPrestadorNome("Prestador nome teste");
		campos.setTelefoneBeneficiario("(61)99999-9999");
		campos.setValor("valor teste");
		
		campos.setEmailBeneficiario("davi.diniz@hepta.com.br");
		campos.setPrestadorEmail("davi.diniz@hepta.com.br");
		
	}

	@Test
	void testEnviarEmailHTMLAmazonCliente() {
		try {
			EmailUtil.enviarEmailAmazon(campos,"",TipoEmailEnum.AGENDAMENTO_CLIENTE);
			assert (true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}
	@Test
	void testEnviarEmailHTMLAmazonAssociado() {
		try {
			EmailUtil.enviarEmailAmazon(campos,"",TipoEmailEnum.AGENDAMENTO_ASSOCIADO);
			assert (true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}

	@Test
	void testEnviarEmailCliente() {
		try {
			EmailUtil.enviarEmail(campos,"",TipoEmailEnum.AGENDAMENTO_CLIENTE);
			assert (true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}

	@Test
	void testEnviarEmailAssociado() {
		try {
			EmailUtil.enviarEmail(campos,"",TipoEmailEnum.AGENDAMENTO_ASSOCIADO);
			assert (true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	void testEnviarEmailVerificacaoHTML() {
		try {
			EmailUtil.enviarEmail(campos,TokenVerificacao.generate(),TipoEmailEnum.AGENDAMENTO_ASSOCIADO);
			assert (true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
}
