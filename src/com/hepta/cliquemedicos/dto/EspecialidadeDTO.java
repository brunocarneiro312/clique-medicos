package com.hepta.cliquemedicos.dto;

import java.io.Serializable;

public class EspecialidadeDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String descricao;
	
	public EspecialidadeDTO() {
		super();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
