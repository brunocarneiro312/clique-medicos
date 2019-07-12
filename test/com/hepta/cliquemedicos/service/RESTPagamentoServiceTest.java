package com.hepta.cliquemedicos.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.cliquemedicos.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hepta.cliquemedicos.dto.ServicoDTO;
import com.hepta.cliquemedicos.dto.pagseguro.PaymentDTO;
import com.hepta.cliquemedicos.entity.Compra;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.persistence.UsuarioDAO;

class RESTPagamentoServiceTest {
	private static HttpServletRequest request;
	private static RESTPagamentoService service;
	private static HttpServletRequest requestFiltros;
	private static RESTFiltrosAMHPService serviceFiltros;
	private static HttpServletRequest requestAssociado;
	private static RESTAssociadosAMHPService serviceAssociados;
	CompraDTO compra = new CompraDTO();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// ARRANGE
		request = Mockito.mock(HttpServletRequest.class);
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		service = new RESTPagamentoService();
		service.setRequest(request);

		requestFiltros = Mockito.mock(HttpServletRequest.class);
		HttpSession sessionFiltro = Mockito.mock(HttpSession.class);
		Mockito.when(requestFiltros.getSession()).thenReturn(sessionFiltro);
		serviceFiltros = new RESTFiltrosAMHPService();
		serviceFiltros.setRequest(request);

		requestAssociado = Mockito.mock(HttpServletRequest.class);
		HttpSession sessionAssociado = Mockito.mock(HttpSession.class);
		Mockito.when(requestAssociado.getSession()).thenReturn(sessionAssociado);
		serviceAssociados = new RESTAssociadosAMHPService();
		serviceAssociados.setRequest(request);
	}

/*	@SuppressWarnings("unchecked")
	@Test
	final void compraGlobalPayments() {
		Response responseFiltro;
		List<EspecialidadeDTO> especialidades = new ArrayList<>();
		ServicoDTO agendamento = new ServicoDTO();
		LocalDateTime dataFiltro = LocalDateTime.now().plusMonths(1).withDayOfMonth(1);

		responseFiltro = serviceFiltros.getEspecialidade();
		if (responseFiltro.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			especialidades = (List<EspecialidadeDTO>) responseFiltro.getEntity();
			if (especialidades.isEmpty())
				fail("Não foi possível buscar especialidades");
		} else {
			fail("Não foi possível buscar especialidades");
		}

		MsgBuscaAssociadoDTO mensagem = new MsgBuscaAssociadoDTO(especialidades.get(0).getId(), new ArrayList<>(),
				dataFiltro.toString(), "");
		Response response = serviceAssociados.buscaAssociados(mensagem);
		List<AssociadosDTO> associados = (List<AssociadosDTO>) response.getEntity();
		if (associados.isEmpty() || response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar associados com filtro de especialidade");

		AssociadosDTO associado = associados.get(0);
		UsuarioDAO dao = new UsuarioDAO();
		Usuario u = null;
		try {
			u = (Usuario) dao.getAll().get(0);
			//Salva usuario na sessao
			Mockito.when(request.getSession().getAttribute("user")).thenReturn(u);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro ao buscar usuario");
		}
		UsuarioDTO usuario = new UsuarioDTO(u);
		agendamento.setInicio(associado.getAgendaApontamentos().get(0).toString());
		compra.setServico(agendamento);
		compra.setUsuario(usuario);
		compra.setPayment(new PaymentDTO()); //TODO::botar mais coisas
		compra.setDate_consulta(LocalDateTime.now());
		compra.setDec_valor_consulta(new BigDecimal(150));
		compra.setStr_matricula_prestador(associado.getPrestador().toString());
		compra.setStr_matricula_credenciado(associado.getCredenciado().toString());
		compra.setStr_nome_associado(associado.getNomePrestador());
		compra.setStr_associado_endereco(associado.getPessoaEnderecoHorario());
		compra.setStr_especialidade(especialidades.get(0).getId().toString());
		compra.setOrdemDeChegada(associado.getOrdemdeChegada());

		response = service.comprarPagSeguro(compra);
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()
				|| response.getStatusInfo().getStatusCode() == 101);
	}
*/
	@Test
	@SuppressWarnings("unchecked")
	final void compraBoleto() {
		Response responseFiltro;
		List<EspecialidadeDTO> especialidades = new ArrayList<>();
		ServicoDTO agendamento = new ServicoDTO();
		LocalDateTime dataFiltro = LocalDateTime.now().plusMonths(1).withDayOfMonth(1);

		responseFiltro = serviceFiltros.getEspecialidade();
		if (responseFiltro.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			especialidades = (List<EspecialidadeDTO>) responseFiltro.getEntity();
			if (especialidades.isEmpty())
				fail("Não foi possível buscar especialidades");
		} else {
			fail("Não foi possível buscar especialidades");
		}

		MsgBuscaAssociadoDTO mensagem = new MsgBuscaAssociadoDTO(especialidades.get(0).getId(), new ArrayList<>(),
				dataFiltro.toString(), "");
		Response response = serviceAssociados.buscaAssociados(mensagem);
		List<AssociadosDTO> associados = (List<AssociadosDTO>) response.getEntity();
		if (associados.isEmpty() || response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar associados com filtro de especialidade");

		AssociadosDTO associado = associados.get(0);
		UsuarioDAO dao = new UsuarioDAO();
		Usuario u = null;
		try {
			u = (Usuario) dao.getAll().get(0);
			//Salva usuario na sessao
			Mockito.when(request.getSession().getAttribute("user")).thenReturn(u);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro ao buscar usuario");
		}
		UsuarioDTO usuario = new UsuarioDTO(u);
		agendamento.setNomeBeneficiario(u.getStr_nome());
		agendamento.setTelefoneBeneficiario(u.getStr_telefone());
		agendamento.setEmailBeneficiario("andressa.valadares@hepta.com.br");
		agendamento.setInicio(associado.getAgendaApontamentos().get(0).toString());
		compra.setServico(agendamento);
		compra.setUsuario(usuario);
		compra.setPayment(new PaymentDTO()); //TODO::botar mais coisas
		compra.setDate_consulta(LocalDateTime.now());
		compra.setDec_valor_consulta(new BigDecimal(150));
		compra.setStr_matricula_prestador(associado.getPrestador().toString());
		compra.setStr_matricula_credenciado(associado.getCredenciado().toString());
		compra.setStr_nome_associado(associado.getNomePrestador());
		compra.setStr_associado_endereco(associado.getPessoaEnderecoHorario());
		compra.setStr_especialidade(especialidades.get(0).getId().toString());
		compra.setOrdemDeChegada(associado.getOrdemdeChegada());

		response = service.compraBoleto(compra);
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()
				|| response.getStatusInfo().getStatusCode() == 101);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	final void compraVoucher() {
		Response responseFiltro;
		List<EspecialidadeDTO> especialidades = new ArrayList<>();
		ServicoDTO agendamento = new ServicoDTO();
		LocalDateTime dataFiltro = LocalDateTime.now().plusMonths(1).withDayOfMonth(1);

		responseFiltro = serviceFiltros.getEspecialidade();
		if (responseFiltro.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			especialidades = (List<EspecialidadeDTO>) responseFiltro.getEntity();
			if (especialidades.isEmpty())
				fail("Não foi possível buscar especialidades");
		} else {
			fail("Não foi possível buscar especialidades");
		}

		MsgBuscaAssociadoDTO mensagem = new MsgBuscaAssociadoDTO(especialidades.get(0).getId(), new ArrayList<>(),
				dataFiltro.toString(), "");
		Response response = serviceAssociados.buscaAssociados(mensagem);
		List<AssociadosDTO> associados = (List<AssociadosDTO>) response.getEntity();
		if (associados.isEmpty() || response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar associados com filtro de especialidade");

		AssociadosDTO associado = associados.get(0);
		UsuarioDAO dao = new UsuarioDAO();
		Usuario u = null;
		try {
			u = (Usuario) dao.getAll().get(0);
			//Salva usuario na sessao
			Mockito.when(request.getSession().getAttribute("user")).thenReturn(u);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro ao buscar usuario");
		}
		UsuarioDTO usuario = new UsuarioDTO(u);
		agendamento.setNomeBeneficiario(u.getStr_nome());
		agendamento.setTelefoneBeneficiario(u.getStr_telefone());
		agendamento.setEmailBeneficiario("andressa.valadares@hepta.com.br");
		agendamento.setInicio(associado.getAgendaApontamentos().get(0).toString());
		compra.setServico(agendamento);
		compra.setUsuario(usuario);
		compra.setPayment(new PaymentDTO()); //TODO::botar mais coisas
		compra.setDate_consulta(LocalDateTime.now());
		compra.setDec_valor_consulta(new BigDecimal(150));
		compra.setStr_matricula_prestador(associado.getPrestador().toString());
		compra.setStr_matricula_credenciado(associado.getCredenciado().toString());
		compra.setStr_nome_associado(associado.getNomePrestador());
		compra.setStr_associado_endereco(associado.getPessoaEnderecoHorario());
		compra.setStr_especialidade(especialidades.get(0).getId().toString());
		compra.setOrdemDeChegada(associado.getOrdemdeChegada());
		compra.setLong_voucher(848067097L);//TODO encontrar um jeito de não fazer isso manualmente (andressa.valadares)
		
		response = service.comprarVoucher(compra);
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());
		//System.out.println(response.readEntity(String.class));
	}
/*
	@Test
	final void registraCompraGPSemAlgunsCamposFalha() {
		UsuarioDAO dao = new UsuarioDAO();
		Usuario u = null;
		try {
			u = (Usuario) dao.getAll().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro ao buscar usuario");
		}
		UsuarioDTO usuario = new UsuarioDTO(u);

		compra.setUsuario(usuario);
		compra.setDate_consulta(LocalDateTime.now());
		compra.setDec_valor_consulta(new BigDecimal(150));
		compra.setStr_matricula_prestador("");
		compra.setStr_matricula_credenciado(null);
		compra.setStr_nome_associado("Dra. Médica");
		compra.setStr_associado_endereco("7617069");
		compra.setStr_especialidade("-864101");

		Response response = service.comprarPagSeguro(compra);
		assertTrue(response.getStatusInfo().getStatusCode() == Status.BAD_REQUEST.getStatusCode());
	}*/

	@SuppressWarnings("unchecked")
	@Test
	final void listaComprasRegistradas() {
		Response response = service.listaRegistradas();
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());
		List<Compra> list = (List<Compra>) response.getEntity();
		for (Compra c : list) {
			assertTrue(c.getLong_voucher() != null);
		}
	}
}
