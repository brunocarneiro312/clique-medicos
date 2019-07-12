package com.hepta.cliquemedicos.dto;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hepta.cliquemedicos.entity.Servico;

public class ServicoDTO {
	private String prestador;
	private String credenciado;
	private String pessoaEndereco; // Campo para diferenciar a localidade que o medico atende no AMHP
	private String especialidade;
	private String nomeBeneficiario;
	private String matriculaBeneficiario;
	private String emailBeneficiario;
	private String telefoneBeneficiario;
	private String inicio;
	@JsonProperty("ordemChegada")
	private Boolean ordemDeChegada;
	@JsonProperty("podeAgendar")
	private Boolean agendavel;
	private Integer idServicoAMHP; // TODO: verificar JsonProperty (bruno.carneiro)

	public ServicoDTO() {
		super();
	}

	public ServicoDTO(CompraDTO a) {
		//TODO
		//this.setMatriculaBeneficiario(a.getUsuario().getStr_cpf());
		this.setMatriculaBeneficiario(a.getUsuario().getId_usuario().toString());
		this.setEmailBeneficiario(a.getUsuario().getStr_email());
		this.setNomeBeneficiario(a.getUsuario().getStr_nome());
		this.setTelefoneBeneficiario(a.getUsuario().getStr_telefone());
		this.setPrestador(a.getStr_matricula_prestador());
		this.setCredenciado(a.getStr_matricula_credenciado());
		this.setInicio(a.getDate_consulta().format(DateTimeFormatter.ISO_DATE_TIME));
		this.setPessoaEndereco(a.getStr_associado_endereco());
		this.setEspecialidade(a.getStr_especialidade());
		this.setOrdemDeChegada(a.getOrdemDeChegada());
	}

	public ServicoDTO(ServicoDTO a) {
		super();
		this.setMatriculaBeneficiario(a.getMatriculaBeneficiario());
		this.setCredenciado(a.getCredenciado());
		this.setEmailBeneficiario(a.getEmailBeneficiario());
		this.setEspecialidade(a.getEspecialidade());
		this.setInicio(a.getInicio());
		this.setNomeBeneficiario(a.getNomeBeneficiario());
		this.setPessoaEndereco(a.getPessoaEndereco());
		this.setPrestador(a.getPrestador());
		this.setTelefoneBeneficiario(a.getTelefoneBeneficiario());
		this.setOrdemDeChegada(a.getOrdemDeChegada());
	}

	public ServicoDTO(Servico a) {
		super();
		//this.setMatriculaBeneficiario(a.getStr_cpf_beneficiario());
		this.setMatriculaBeneficiario(a.getCompra().getUsuario().getId_usuario().toString());
		this.setCredenciado(a.getStr_credenciado());
		this.setEmailBeneficiario(a.getStr_email_beneficiario());
		this.setEspecialidade(a.getStr_especialidade());
		this.setInicio(a.getDate_inicio().format(DateTimeFormatter.ISO_DATE_TIME));
		this.setNomeBeneficiario(a.getStr_nome_beneficiario());
		this.setPessoaEndereco(a.getStr_pessoa_endereco());
		this.setPrestador(a.getStr_prestador());
		this.setTelefoneBeneficiario(a.getStr_telefone_beneficiario());
		this.setOrdemDeChegada(a.getBool_ordem_chegada());
	}

	public Boolean getOrdemDeChegada() {
		return ordemDeChegada;
	}

	public void setOrdemDeChegada(Boolean ordemDeChegada) {
		this.ordemDeChegada = ordemDeChegada;
	}

	public String getPrestador() {
		return prestador;
	}

	public void setPrestador(String prestador) {
		this.prestador = prestador;
	}

	public String getCredenciado() {
		return credenciado;
	}

	public void setCredenciado(String credenciado) {
		this.credenciado = credenciado;
	}

	public String getPessoaEndereco() {
		return pessoaEndereco;
	}

	public void setPessoaEndereco(String pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}

	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public String getMatriculaBeneficiario() {
		return matriculaBeneficiario;
	}

	public void setMatriculaBeneficiario(String matriculaBeneficiario) {
		this.matriculaBeneficiario = matriculaBeneficiario;
	}

	public String getEmailBeneficiario() {
		return emailBeneficiario;
	}

	public void setEmailBeneficiario(String emailBeneficiario) {
		this.emailBeneficiario = emailBeneficiario;
	}

	public String getTelefoneBeneficiario() {
		return telefoneBeneficiario;
	}

	public void setTelefoneBeneficiario(String telefoneBeneficiario) {
		this.telefoneBeneficiario = telefoneBeneficiario;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public Boolean getAgendavel() {
		return agendavel;
	}

	public void setAgendavel(Boolean agendavel) {
		this.agendavel = agendavel;
	}

	public Integer getIdServicoAMHP() {
		return idServicoAMHP;
	}

	public void setIdServicoAMHP(Integer idServicoAMHP) {
		this.idServicoAMHP = idServicoAMHP;
	}
}
