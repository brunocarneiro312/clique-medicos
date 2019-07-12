package com.hepta.cliquemedicos.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.cliquemedicos.dto.AgendaApontamentosDTO;
import com.hepta.cliquemedicos.dto.AssociadosDTO;
import com.hepta.cliquemedicos.dto.MsgBuscaAssociadoDTO;
import com.hepta.cliquemedicos.util.AmbienteUtil;

@Path("associados")
public class RESTAssociadosAMHPService {
	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	private Client client = ClientBuilder.newClient();

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@POST
	@Path("busca")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response buscaAssociados(MsgBuscaAssociadoDTO mensagem) {
		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/Associados").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY).post(Entity.json(mensagem));

		if (responseAMHP.getStatus() == 200) {
			LocalDateTime data = LocalDateTime.parse(mensagem.getData(), DateTimeFormatter.ISO_DATE_TIME);
			List<AssociadosDTO> lista = responseAMHP.readEntity(new GenericType<List<AssociadosDTO>>() {

			});
			// Retorna erro caso a lista seja vazia
			if (lista.size() == 0) {
				return Response.status(Status.SERVICE_UNAVAILABLE).build();
			}
			lista = filtraAssociadosPorMes(lista, data);
			return Response.ok().entity(lista).build();
		}
		return responseAMHP;
	}

	private List<AssociadosDTO> filtraAssociadosPorMes(List<AssociadosDTO> lista, LocalDateTime periodo) {
		List<AssociadosDTO> associadosMes = new ArrayList<>();
		for (AssociadosDTO associado : lista) {
			List<AgendaApontamentosDTO> agendaAssociadoMes = new ArrayList<>();
			associado.getAgendaApontamentos().sort(Comparator.comparing(AgendaApontamentosDTO::getInicio));
			for (AgendaApontamentosDTO agenda : associado.getAgendaApontamentos()) {

				//Como esta tudo em date nao tem problema comparar o metodo depracado
				if (agenda.getInicio().getMonth() == periodo.getMonth()
						&& agenda.getInicio().getYear() == periodo.getYear()) {
					agendaAssociadoMes.add(agenda);
				}
			}
			// Se existe horario na agenda, salva o associado na lista
			if (!agendaAssociadoMes.isEmpty()) {
				associado.setAgendaApontamentos(agendaAssociadoMes);
				associadosMes.add(associado);
			}
		}
		return associadosMes;
	}
}
