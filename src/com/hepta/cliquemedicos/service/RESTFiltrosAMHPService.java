package com.hepta.cliquemedicos.service;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.cliquemedicos.dto.EspecialidadeDTO;
import com.hepta.cliquemedicos.dto.LocalidadeDTO;
import com.hepta.cliquemedicos.dto.ValorConsultaDTO;
import com.hepta.cliquemedicos.util.AmbienteUtil;

@Path("filtro")
public class RESTFiltrosAMHPService {
	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	private Client client = ClientBuilder.newClient();

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@GET
	@Path("/especialidade")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getEspecialidade() {
		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/Especialidades").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY).get();

		if (responseAMHP.getStatus() == 200) {
			List<EspecialidadeDTO> lista = responseAMHP.readEntity(new GenericType<List<EspecialidadeDTO>>() {

			});
			// Retorna erro caso a lista seja vazia
			if (lista.size() == 0) {
				return Response.status(Status.SERVICE_UNAVAILABLE).build();
			}
			return Response.ok().entity(lista).build();
		}
		return responseAMHP;
	}

	@GET
	@Path("/localidade/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response listaLocalidadesPorEspecialidade(@PathParam("id") Integer especialidadeId) {
		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/Localidades/" + especialidadeId)
				.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY)
				.get();

		if (responseAMHP.getStatus() == 200) {
			List<LocalidadeDTO> lista = responseAMHP.readEntity(new GenericType<List<LocalidadeDTO>>() {

			});
			// Retorna erro caso a lista seja vazia
			if (lista.size() == 0) {
				return Response.status(Status.SERVICE_UNAVAILABLE).build();
			}
			return Response.ok().entity(lista).build();
		}
		return responseAMHP;

	}

	@GET
	@Path("/especialidadeValor/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPrecoEspecialidade(@PathParam("id") Integer especialidadeId) {
		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/Procedimento/ObterValorConsulta")
				.queryParam("especialidadeId", especialidadeId).request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY).get();
		if (responseAMHP.getStatus() == 200) {
			ValorConsultaDTO valorConsulta = responseAMHP.readEntity(ValorConsultaDTO.class);
			return Response.ok().entity(valorConsulta.getValor()).build();
		}
		return responseAMHP;
	}
}
