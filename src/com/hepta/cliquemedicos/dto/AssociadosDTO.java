package com.hepta.cliquemedicos.dto;

import java.util.List;

public class AssociadosDTO {
	// ids
	Integer matriculaCredenciado;
	Integer matriculaPrestador;
	// clinica
	String nomeCredenciado;
	//medico
	String nomePrestador;
	Integer credenciado;
	Integer prestador;
	String endereco;
	String bairrodesc;
	String uf;
	Boolean ordemdeChegada;
	String pessoaEnderecoHorario; // Campo para diferenciar a localidade que o medico atende no AMHP
	String contato;
	String sexo; // Se o prestador e dr ou dra
	Boolean fimDeSemana;
	Integer prestadorProfissional;
	List<AgendaApontamentosDTO> agendaApontamentos;
	
	public AssociadosDTO() {
		super();
	}

	public Boolean getFimDeSemana() {
		return fimDeSemana;
	}

	public void setFimDeSemana(Boolean fimDeSemana) {
		this.fimDeSemana = fimDeSemana;
	}

	public Integer getPrestadorProfissional() {
		return prestadorProfissional;
	}

	public void setPrestadorProfissional(Integer prestadorProfissional) {
		this.prestadorProfissional = prestadorProfissional;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Integer getMatriculaCredenciado() {
		return matriculaCredenciado;
	}

	public void setMatriculaCredenciado(Integer matriculaCredenciado) {
		this.matriculaCredenciado = matriculaCredenciado;
	}

	public Integer getMatriculaPrestador() {
		return matriculaPrestador;
	}

	public void setMatriculaPrestador(Integer matriculaPrestador) {
		this.matriculaPrestador = matriculaPrestador;
	}

	public String getNomeCredenciado() {
		return nomeCredenciado;
	}

	public void setNomeCredenciado(String nomeCredenciado) {
		this.nomeCredenciado = nomeCredenciado;
	}

	public String getNomePrestador() {
		return nomePrestador;
	}

	public void setNomePrestador(String nomePrestador) {
		this.nomePrestador = nomePrestador;
	}

	public Integer getCredenciado() {
		return credenciado;
	}

	public void setCredenciado(Integer credenciado) {
		this.credenciado = credenciado;
	}

	public Integer getPrestador() {
		return prestador;
	}

	public void setPrestador(Integer prestador) {
		this.prestador = prestador;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairrodesc() {
		return bairrodesc;
	}

	public void setBairrodesc(String bairrodesc) {
		this.bairrodesc = bairrodesc;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Boolean getOrdemdeChegada() {
		return ordemdeChegada;
	}

	public void setOrdemdeChegada(Boolean ordemdeChegada) {
		this.ordemdeChegada = ordemdeChegada;
	}

	public String getPessoaEnderecoHorario() {
		return pessoaEnderecoHorario;
	}

	public void setPessoaEnderecoHorario(String pessoaEnderecoHorario) {
		this.pessoaEnderecoHorario = pessoaEnderecoHorario;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public List<AgendaApontamentosDTO> getAgendaApontamentos() {
		return agendaApontamentos;
	}

	public void setAgendaApontamentos(List<AgendaApontamentosDTO> agendaApontamentos) {
		this.agendaApontamentos = agendaApontamentos;
	}




}
