package com.hepta.cliquemedicos.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.cliquemedicos.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.hepta.cliquemedicos.dto.ServicoDTO;

public class RESTAmhpServicesTest {
	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	private String API_KEY = "28236d8ec201df516d0f6472d516d72d";

	private static final String REST_URI = "https://cliquemedicoshmg.amhp.com.br/api/3";

	private Client client = ClientBuilder.newClient();

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@Test
	final void agendamentoComSucesso() {
		LocalDate today = LocalDate.now();
		Response responseEspecialidades;
		Response responseAssociados;
		Response responseAgenda;
		Response responseCancelamento;
		MsgBuscaAssociadoDTO mensagem;
		ServicoDTO agendamento = new ServicoDTO();
		AssociadosDTO associado = new AssociadosDTO();
		CamposEmailDTO agendamentoConfirmado;
		List<EspecialidadeDTO> especialidades = new ArrayList<EspecialidadeDTO>();

		responseEspecialidades = client.target(REST_URI).path("/Especialidades").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", API_KEY).get();
		if (responseEspecialidades.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar as especialidades");

		especialidades = responseEspecialidades.readEntity(new GenericType<List<EspecialidadeDTO>>() {
		});
		mensagem = new MsgBuscaAssociadoDTO(especialidades.get(0).getId(), new ArrayList<>(), today.toString(), "");

		responseAssociados = client.target(REST_URI).path("/Associados").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", API_KEY).post(Entity.json(mensagem));

		if (responseAssociados.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível buscar associados");
		
		associado = responseAssociados.readEntity(new GenericType<List<AssociadosDTO>>() {
		}).get(0);

		agendamento.setMatriculaBeneficiario("1");
		agendamento.setTelefoneBeneficiario("61987456987");
		agendamento.setNomeBeneficiario("Teste Junit AMHP");
		agendamento.setEmailBeneficiario("andressa.valadares@hepta.com.br");
		agendamento.setEspecialidade(especialidades.get(0).getId().toString());
		agendamento.setCredenciado(associado.getCredenciado().toString());
		agendamento.setInicio(associado.getAgendaApontamentos().get(0).getInicio().toString());
		agendamento.setPessoaEndereco(associado.getPessoaEnderecoHorario());
		agendamento.setPrestador(associado.getPrestador().toString());
		agendamento.setOrdemDeChegada(associado.getOrdemdeChegada());

		responseAgenda = client.target(REST_URI).path("/AgendaConsulta").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", API_KEY).post(Entity.json(agendamento));

		if (responseAgenda.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
			fail("Não foi possível agendar um horário");
		
		agendamentoConfirmado = responseAgenda.readEntity(CamposEmailDTO.class);

		if (agendamentoConfirmado.getId() == null || agendamentoConfirmado.getEnderecoAtendimento() == null
				|| agendamentoConfirmado.getPrestadorEmail() == null || agendamentoConfirmado.getInicio() == null)
			fail("Não foi possível agendar um horário");

		responseCancelamento = client.target(REST_URI).path("/AgendaConsulta/" + agendamentoConfirmado.getId())
				.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("api-key", API_KEY)
				.delete();

		assertTrue(responseCancelamento.getStatusInfo().getStatusCode() == Status.OK.getStatusCode());

	}

}
