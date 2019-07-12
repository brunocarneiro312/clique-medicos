package com.hepta.cliquemedicos.dto.boleto;

import java.math.BigDecimal;

public class RequestBoletoDTO {
	private String nome;
	private String cpfcnpj;
	private String endereco;
	private String bairro;
	private String cidade;
	private String cep;
	private String uf;
	private BigDecimal valorBoleto;
	
	public RequestBoletoDTO(RequestBoletoDTO a) {
		super();
		this.setBairro(a.getBairro());
		this.setCep(a.getCep());
		this.setCidade(a.getCidade());
		this.setCpfcnpj(a.getCpfcnpj());
		this.setEndereco(a.getEndereco());
		this.setNome(a.getNome());
		this.setUf(a.getUf());
		this.setValorBoleto(a.getValorBoleto());
	}

	public RequestBoletoDTO() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public BigDecimal getValorBoleto() {
		return valorBoleto;
	}

	public void setValorBoleto(BigDecimal valorBoleto) {
		this.valorBoleto = valorBoleto;
	}

}
