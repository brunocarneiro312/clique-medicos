package com.hepta.cliquemedicos.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hepta.cliquemedicos.dto.ServicoDTO;
import com.hepta.cliquemedicos.dto.boleto.RequestBoletoDTO;

/**
 * 
 * Classe responsável por fornecer métodos utilitários de acesso a API de terceiros
 * 
 * @author bruno.carneiro
 *
 */
public class ResourceUtil {
	
	/**
	 * 
	 * Consulta API da AMHP para obter os valores da consulta pela especialidade
	 * 
	 * @param strEspecialidade
	 * 		Código da especialidade no formato String
	 * 
	 * @return
	 * 		Retorna o Response
	 */
	public static Response callObterValorConsulta(String strEspecialidade) {
		
		final String QUERY_PARAM_NAME = "especialidadeId";
		
		Client client = ClientBuilder.newClient();
		
		Response response = client
			.target(AmbienteUtil.AHMP_REST_URI)
			.path(AmbienteUtil.PATH_OBTER_VALOR_CONSULTA)
			.queryParam(QUERY_PARAM_NAME, strEspecialidade)
			.request(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.header(AmbienteUtil.API_KEY_NAME, AmbienteUtil.AHMP_API_KEY)
			.get();
		
		return response;
		
	}
	
	/**
	 * 
	 * Solicita à API da AMHP a geração de um boleto
	 * 
	 * @param requestBoletoDTO
	 * 		Dados do boleto a ser gerado
	 * 
	 * @return
	 * 		Retorna o Response
	 */
	public static Response callGerarBoleto(RequestBoletoDTO requestBoletoDTO) {
		
		Client client = ClientBuilder.newClient();
		
		Response response = client
				.target(AmbienteUtil.AHMP_REST_URI)
				.path(AmbienteUtil.PATH_GERAR_BOLETO)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(AmbienteUtil.API_KEY_NAME, AmbienteUtil.AHMP_API_KEY)
				.post(Entity.json(requestBoletoDTO));
		
		return response;
		
	}
	
	/**
	 * 
	 * Solicita à API da AMHP o agendamento de uma consulta
	 * 
	 * @param AgendamentoDTO
	 * 		Dados do agendamento
	 * 
	 * @return
	 * 		Retorna o Response
	 */
	public static Response callAgendarConsulta(ServicoDTO servicoDTO) {
		
		Client client = ClientBuilder.newClient();
		
		Response response = client
				.target(AmbienteUtil.AHMP_REST_URI)
				.path(AmbienteUtil.PATH_AGENDAR_CONSULTA)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("api-key", AmbienteUtil.AHMP_API_KEY)
				.post(Entity.json(servicoDTO));
		
		return response;
		
	}
	
	public static Response callGetAssociadoAMHP(String especialidadeId, String credenciado, String prestador, String pessoaEndereco) throws Exception{
		Client client = ClientBuilder.newClient();
		
		Response responseAMHP = client
				.target(AmbienteUtil.AHMP_REST_URI)
				.path("/Associados")
				.queryParam("especialidadeId", especialidadeId)
				.queryParam("credenciado", credenciado)
				.queryParam("prestador", prestador)
				.queryParam("pessoaEndereco", pessoaEndereco)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("api-key", AmbienteUtil.AHMP_API_KEY)
				.get();
		
		return responseAMHP;
	}
	
	public static Response callGetEspecialidadeAMHP(String especialidadeId) throws Exception{
		Client client = ClientBuilder.newClient();
		Response responseAMHP = client
				.target(AmbienteUtil.AHMP_REST_URI)
				.path("/Especialidades/" + especialidadeId)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("api-key", AmbienteUtil.AHMP_API_KEY)
				.get();
		
		return responseAMHP;

	}

	
}
