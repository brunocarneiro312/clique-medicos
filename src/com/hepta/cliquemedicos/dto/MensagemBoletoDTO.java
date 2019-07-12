package com.hepta.cliquemedicos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hepta.cliquemedicos.entity.Boleto;

public class MensagemBoletoDTO {
	private Integer idBoleto;
	@JsonProperty("id")
	private Integer idAMHP;
	private String urlBoleto;
	private String codigoBarras;
	private String statusDescricao;
	private String status;

	public MensagemBoletoDTO(MensagemBoletoDTO a) {
		super();
		this.setCodigoBarras(a.getCodigoBarras());
		this.setIdBoleto(a.getIdBoleto());
		this.setIdAMHP(a.getIdAMHP());
		this.setStatus(a.getStatus());
		this.setStatusDescricao(a.getStatusDescricao());
		this.setUrlBoleto(a.getUrlBoleto());
	}
	
	public MensagemBoletoDTO(Boleto a) {
		super();
		this.setCodigoBarras(a.getStr_codigo_barras());
		this.setIdBoleto(a.getId_boleto());
		this.setIdAMHP(a.getId_amhp());
		this.setStatus(a.getStr_status());
		this.setStatusDescricao(a.getStr_status_descricao());
		this.setUrlBoleto(a.getStr_url());
	}
	
	public MensagemBoletoDTO() {
		super();
	}

	public Integer getIdBoleto() {
		return idBoleto;
	}

	public void setIdBoleto(Integer idBoleto) {
		this.idBoleto = idBoleto;
	}

	public Integer getIdAMHP() {
		return idAMHP;
	}

	public void setIdAMHP(Integer idAMHP) {
		this.idAMHP = idAMHP;
	}

	public String getUrlBoleto() {
		return urlBoleto;
	}

	public void setUrlBoleto(String urlBoleto) {
		this.urlBoleto = urlBoleto;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getStatusDescricao() {
		return statusDescricao;
	}

	public void setStatusDescricao(String statusDescricao) {
		this.statusDescricao = statusDescricao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
