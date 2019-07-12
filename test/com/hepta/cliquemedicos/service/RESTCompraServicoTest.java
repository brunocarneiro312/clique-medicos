package com.hepta.cliquemedicos.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.persistence.UsuarioDAO;

class RESTCompraServicoTest {
	private static HttpServletRequest request;
	private static RESTDadosCompraService service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// ARRANGE
		request = Mockito.mock(HttpServletRequest.class);
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		service = new RESTDadosCompraService();
		service.setRequest(request);
	}

	@Test
	final void testBuscaHistorico() {
		UsuarioDAO dao = new UsuarioDAO();
		Usuario usuario = null;
		try {
			usuario = (Usuario) dao.getAll().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro ao buscar usuario");
		}

		Mockito.when(request.getSession().getAttribute("user")).thenReturn(usuario);
		Response response = service.historico();
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());
		System.out.println(response.getEntity());
	}

	@Test
	final void testBuscaAgendamentosFuturos() {
		UsuarioDAO dao = new UsuarioDAO();
		Usuario usuario = null;
		try {
			usuario = (Usuario) dao.getAll().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro ao buscar usuario");
		}

		Mockito.when(request.getSession().getAttribute("user")).thenReturn(usuario);
		Response response = service.agendamentos();
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());
		System.out.println(response.getEntity());
	}
}
