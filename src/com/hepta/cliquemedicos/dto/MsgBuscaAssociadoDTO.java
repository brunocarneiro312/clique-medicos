package com.hepta.cliquemedicos.dto;

import java.util.List;

public class MsgBuscaAssociadoDTO {
	private Integer especialidadeId;
	private List<Integer> bairroId;
	private String data;
	private String nome;

	public MsgBuscaAssociadoDTO() {

	}

	public MsgBuscaAssociadoDTO(Integer especialidadeId, List<Integer> bairroId, String data, String nome) {
		this.setBairroId(bairroId);
		this.setEspecialidadeId(especialidadeId);
		this.setData(data);
		this.setNome(nome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getEspecialidadeId() {
		return especialidadeId;
	}

	public void setEspecialidadeId(Integer especialidadeId) {
		this.especialidadeId = especialidadeId;
	}

	public List<Integer> getBairroId() {
		return bairroId;
	}

	public void setBairroId(List<Integer> bairroId) {
		this.bairroId = bairroId;
	}

}
