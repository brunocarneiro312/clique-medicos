package com.hepta.cliquemedicos.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hepta.cliquemedicos.dto.EspecialidadeDTO;
import com.hepta.cliquemedicos.dto.LocalidadeDTO;

class RESTFiltrosAMHPServiceTest {
	private static HttpServletRequest request;
	private static RESTFiltrosAMHPService service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// ARRANGE
		request = Mockito.mock(HttpServletRequest.class);
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		service = new RESTFiltrosAMHPService();
		service.setRequest(request);
	}

	@SuppressWarnings("unchecked")
	@Test
	final void getTodasEspecialidades() {
		Response response = service.getEspecialidade();
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());
		List<EspecialidadeDTO> especialidades = (List<EspecialidadeDTO>) response.getEntity();
		assertTrue(especialidades.get(0).getId() != null);
		assertTrue(especialidades.get(0).getId() != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	final void getLocalidadesPorEspecialidade() {
		EspecialidadeDTO especialidade = new EspecialidadeDTO();
		especialidade.setId(-864219);
		especialidade.setDescricao("Reumatologia");

		Response response = service.listaLocalidadesPorEspecialidade(especialidade.getId());
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());
		List<LocalidadeDTO> localidades = (List<LocalidadeDTO>) response.getEntity();
		assertTrue(localidades.get(0).getId() != null);
		assertTrue(localidades.get(0).getDescricao() != null);
	}
	
	@Test
	final void getPrecoEspecialidade() {
		EspecialidadeDTO especialidade = new EspecialidadeDTO();
		especialidade.setId(-864219);
		especialidade.setDescricao("Reumatologia");

		Response response = service.getPrecoEspecialidade(especialidade.getId());
		assertTrue(response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());
		Double resultado =  (Double) response.getEntity();
		assertTrue(resultado != null);
	}
}
