package com.hepta.cliquemedicos.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AmbienteUtil {
	
	//DESENVOLVIMENTO
	public static final String ENDERECO_EMAIL_VERIFICACAO = "http://localhost:8080/CliqueMedicos/rs/cadastro/verificar?token=";
	public static final String ENDERECO_EMAIL_ALTERACAO_SENHA = "http://localhost:8080/CliqueMedicos/rs/cadastro/enviar-email-recuperacao/confirmar?token=";
	public static final String PERSISTENCE_UNIT_NAME = "cm-desenvolvimento";
	
	/*
	// HOMOLOGACAO
	public static final String ENDERECO_EMAIL_VERIFICACAO = "https://salitre.hepta.com.br/CliqueMedicos/rs/cadastro/verificar?token=";
	public static final String ENDERECO_EMAIL_ALTERACAO_SENHA = "https://salitre.hepta.com.br/CliqueMedicos/rs/cadastro/enviar-email-recuperacao/confirmar?token=";
	public static final String PERSISTENCE_UNIT_NAME = "cm-homologacao";
	*/
	/*
	//PRODUCAO
	public static final String ENDERECO_EMAIL_VERIFICACAO = "https://sistemas.hepta.com.br/CliqueMedicos/rs/cadastro/verificar?token=";
	public static final String ENDERECO_EMAIL_ALTERACAO_SENHA = "https://sistemas.hepta.com.br/CliqueMedicos/rs/cadastro/enviar-email-recuperacao/confirmar?token=";
	public static final String PERSISTENCE_UNIT_NAME = "cm-producao";
	 */
	public static final String CAMINHO_IMAGEM = "http://www.hepta.com.br/Public/email-cliquemedicos/";
	public static final String ENDERECO_PAG_SEGURO  = "https://ws.sandbox.pagseguro.uol.com.br/v2/";
	public static final String EMAIL_PAG_SEGURO  = "davi.diniz@hepta.com.br";
	public static final String TOKEN_PAG_SEGURO  = "896CA31D8EA148B795285F3A36263015";
	public static final String AHMP_REST_URI = "https://develop.cliquemedicos.com.br/api/4"; // TODO desenvolvimento
	//public static final String AHMP_REST_URI = "https://cliquemedicoshmg.amhp.com.br/api/4";
	public static final String API_KEY_NAME = "api-key";
	public static final String AHMP_API_KEY = "28236d8ec201df516d0f6472d516d72d";
	public static final int TEMPO_JOB_PAGSEGURO = 60; //em segundos
	//EXECUTANDO O JOB 1X POR DIA
	public static final LocalDateTime PERIODO_FINAL_JOB = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
	public static final LocalDateTime PERIODO_INICIAL_JOB = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(1);
	public static final int TEMPO_JOB_BOLETO = 60; //EM SEGUNDOS TODO trocar para apenas 1x ao dia
	
	/**
	 * ---------
	 * ENDPOINTS
	 * ---------
	 */
	
	public static final String PATH_OBTER_VALOR_CONSULTA = "/Procedimento/ObterValorConsulta";
	public static final String PATH_GERAR_BOLETO         = "/Boleto";
	public static final String PATH_AGENDAR_CONSULTA     = "/AgendaConsulta";
}
