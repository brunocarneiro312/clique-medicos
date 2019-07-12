package com.hepta.cliquemedicos.service;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hepta.cliquemedicos.dto.AgendaApontamentosDTO;
import com.hepta.cliquemedicos.dto.AssociadosDTO;
import com.hepta.cliquemedicos.dto.EspecialidadeDTO;
import com.hepta.cliquemedicos.dto.LocalidadeDTO;
import com.hepta.cliquemedicos.dto.MsgBuscaAssociadoDTO;

class RESTAssociadosAMHPServiceTest {
	private static HttpServletRequest request;
	private static RESTAssociadosAMHPService service;
	private static RESTFiltrosAMHPService serviceFiltros;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// ARRANGE
		request = Mockito.mock(HttpServletRequest.class);
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);

		service = new RESTAssociadosAMHPService();
		serviceFiltros = new RESTFiltrosAMHPService();

		service.setRequest(request);
		serviceFiltros.setRequest(request);
	}

	@SuppressWarnings("unchecked")
	@Test
	final void testBuscaAssociadosPorEspecialidadeSemLocalidade() {
		List<EspecialidadeDTO> especialidades = new ArrayList<>();
		LocalDateTime dataFiltro = LocalDateTime.now().withDayOfMonth(1);
		Response responseFiltro = serviceFiltros.getEspecialidade();
		if (responseFiltro.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			especialidades = (List<EspecialidadeDTO>) responseFiltro.getEntity();
			if (especialidades.isEmpty())
				fail("Não foi possível buscar especialidades");
		} else {
			fail("Não foi possível buscar especialidades");
		}

		// Busca sem localidades
		MsgBuscaAssociadoDTO mensagem = new MsgBuscaAssociadoDTO(especialidades.get(0).getId(), new ArrayList<>(),
				dataFiltro.toString(), "");
		Response response = service.buscaAssociados(mensagem);
		List<AssociadosDTO> associados = (List<AssociadosDTO>) response.getEntity();
		if (associados.isEmpty() || response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar associados com filtro de especialidade");

		assert (true);
	}

	@SuppressWarnings("unchecked")
	@Test
	final void testBuscaAssociadosPorEspecialidadeELocalidade() {
		List<EspecialidadeDTO> especialidades = new ArrayList<>();
		List<Integer> localidades = new ArrayList<>();
		LocalDateTime dataFiltro = LocalDateTime.now().withDayOfMonth(1);
		Response responseFiltro = serviceFiltros.getEspecialidade();
		if (responseFiltro.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			especialidades = (List<EspecialidadeDTO>) responseFiltro.getEntity();
			if (especialidades.isEmpty())
				fail("Não foi possível buscar especialidades");
		} else {
			fail("Não foi possível buscar especialidades");
		}

		responseFiltro = serviceFiltros.listaLocalidadesPorEspecialidade(especialidades.get(0).getId());
		if (responseFiltro.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			List<LocalidadeDTO> aux = (List<LocalidadeDTO>) responseFiltro.getEntity();
			for (LocalidadeDTO l : aux) {
				localidades.add(l.getId());
			}
			if (localidades.isEmpty())
				fail("Não foi possível buscar localidades");
		} else {
			fail("Não foi possível buscar localidades");
		}

		//Busca com localidades
		MsgBuscaAssociadoDTO mensagem = new MsgBuscaAssociadoDTO(especialidades.get(0).getId(), localidades,
				dataFiltro.toString(), "");
		Response response = service.buscaAssociados(mensagem);
		List<AssociadosDTO> associados = (List<AssociadosDTO>) response.getEntity();
		if (associados.isEmpty() || response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar associados com filtro de especialidade e localidades");

		assert(true);
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	final void testBuscaAssociadosPorMes() {
		List<EspecialidadeDTO> especialidades = new ArrayList<>();
		LocalDateTime data = LocalDateTime.of(2019, Month.MARCH, 1, 10, 10, 30);

		Response responseFiltro = serviceFiltros.getEspecialidade();
		if (responseFiltro.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			especialidades = (List<EspecialidadeDTO>) responseFiltro.getEntity();
			if (especialidades.isEmpty())
				fail("Não foi possível buscar especialidades");
		} else {
			fail("Não foi possível buscar especialidades");
		}

		// Busca sem localidades
		MsgBuscaAssociadoDTO mensagem = new MsgBuscaAssociadoDTO(especialidades.get(0).getId(), new ArrayList<>(),
				data.toString(), "");
		Response response = service.buscaAssociados(mensagem);
		List<AssociadosDTO> associados = (List<AssociadosDTO>) response.getEntity();
		if (associados.isEmpty() || response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar associados com filtro de especialidade");

		for (AssociadosDTO a : associados) {
			for (AgendaApontamentosDTO agenda : a.getAgendaApontamentos()) {
				if (agenda.getInicio().getMonth() != data.getMonth()
						|| agenda.getInicio().getYear() != data.getYear()) {
					System.out.println(agenda.getInicio().getMonth() + "/" + agenda.getInicio().getYear() + " e "
							+ data.getMonth() + "/" + data.getYear());
					fail("Existe item na agenda fora do mês e ano desejados");
				}
			}
		}

		assert (true);
	}
}
