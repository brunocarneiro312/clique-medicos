package com.hepta.cliquemedicos.dto;

import com.hepta.cliquemedicos.entity.Compra;

public class RequestAssociadoDTO {
	String especialidadeId;
	String credenciado;
	String prestador;
	String pessoaEndereco;
	
	public RequestAssociadoDTO() {
		super();
	}
	
	public RequestAssociadoDTO(Compra c) {
		this.setCredenciado(c.getStr_matricula_credenciado());
		this.setEspecialidadeId(c.getStr_especialidade());
		this.setPessoaEndereco(c.getStr_associado_endereco());
		this.setPrestador(c.getStr_matricula_prestador());
	}
	
	public String getEspecialidadeId() {
		return especialidadeId;
	}

	public void setEspecialidadeId(String especialidadeId) {
		this.especialidadeId = especialidadeId;
	}

	public String getCredenciado() {
		return credenciado;
	}

	public void setCredenciado(String credenciado) {
		this.credenciado = credenciado;
	}

	public String getPrestador() {
		return prestador;
	}

	public void setPrestador(String prestador) {
		this.prestador = prestador;
	}

	public String getPessoaEndereco() {
		return pessoaEndereco;
	}

	public void setPessoaEndereco(String pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}
	
	
}
