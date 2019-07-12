package com.hepta.cliquemedicos.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.util.LocalDateTimeDeserializer;
import com.hepta.cliquemedicos.util.LocalDateTimeSerializer;

public class UsuarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id_usuario;
	private String str_senha;
	private String str_nome;
	private String str_cpf;
	private String str_email;
	private String str_telefone;
	private String str_endereco;
	private String str_cep;
	private String str_logradouro;
	private String str_complemento;
	private String str_bairro;
	private String str_localidade;
	private String str_uf;
	private String str_unidade;
	private String str_token_verificacao;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime data_nascimento;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime data_token;
	
	private Boolean bool_verificado;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime data_registro;
	
	private List<CompraDTO> compras = new ArrayList<>();

	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(Usuario a) {
		
		super();
		
		this.setId_usuario           (a.getId_usuario());
		this.setStr_cep              (a.getStr_cep());
		this.setStr_cpf              (a.getStr_cpf());
		this.setStr_email            (a.getStr_email());
		this.setStr_endereco         (a.getStr_endereco());
		this.setStr_nome             (a.getStr_nome());
		this.setStr_telefone         (a.getStr_telefone());
		this.setData_registro        (a.getData_registro());
		this.setData_nascimento      (a.getData_nascimento());
		this.setBool_verificado      (a.getBool_verificado());
		this.setStr_token_verificacao(a.getStr_token_verificacao());
		this.setData_token           (a.getData_token());
		this.setStr_logradouro       (a.getStr_logradouro());
		this.setStr_complemento      (a.getStr_complemento());
		this.setStr_bairro           (a.getStr_bairro());
		this.setStr_localidade       (a.getStr_localidade());
		this.setStr_uf               (a.getStr_uf());
		this.setStr_unidade          (a.getStr_unidade());
	}

	public UsuarioDTO(UsuarioDTO a) {
		
		super();
		
		this.setId_usuario           (a.getId_usuario());
		this.setStr_cep              (a.getStr_cep());
		this.setStr_cpf              (a.getStr_cpf());
		this.setStr_email            (a.getStr_email());
		this.setStr_endereco         (a.getStr_endereco());
		this.setStr_nome             (a.getStr_nome());
		this.setStr_telefone         (a.getStr_telefone());
		this.setData_registro        (a.getData_registro());
		this.setData_nascimento      (a.getData_nascimento());
		this.setBool_verificado      (a.getBool_verificado());
		this.setStr_token_verificacao(a.getStr_token_verificacao());
		this.setData_token           (a.getData_token());
		this.setStr_logradouro       (a.getStr_logradouro());
		this.setStr_complemento      (a.getStr_complemento());
		this.setStr_bairro           (a.getStr_bairro());
		this.setStr_localidade       (a.getStr_localidade());
		this.setStr_uf               (a.getStr_uf());
		this.setStr_unidade          (a.getStr_unidade());
	}

	public void limparDados() {
		//this.setId_usuario("");
		this.setStr_cep("");
		//this.setStr_cpf("");
		//this.setStr_email("");
		this.setStr_endereco("");
		this.setStr_nome("");
		this.setStr_senha("");
		this.setStr_telefone("");
		this.setData_registro(null);
	}

	public Boolean checarCampos() {
		if(this.getStr_cep() == null
		|| this.getStr_cpf() == null
		|| this.getStr_email() == null
		|| this.getStr_endereco() == null
		|| this.getStr_nome() == null
		|| this.getStr_senha() == null
		|| this.getStr_telefone() == null
		|| this.getStr_cep().isEmpty()
		|| this.getStr_cpf().isEmpty()
		|| this.getStr_email().isEmpty()
		|| this.getStr_endereco().isEmpty()
		|| this.getStr_nome().isEmpty()
		|| this.getStr_senha().isEmpty()
		|| this.getStr_telefone().isEmpty())
			return false;
		else
			return true;
	}

	public String getStr_token_verificacao() {
		return str_token_verificacao;
	}

	public void setStr_token_verificacao(String str_token_verificacao) {
		this.str_token_verificacao = str_token_verificacao;
	}

	public Boolean getBool_verificado() {
		return bool_verificado;
	}

	public void setBool_verificado(Boolean bool_verificado) {
		this.bool_verificado = bool_verificado;
	}

	public LocalDateTime getData_registro() {
		return data_registro;
	}

	public void setData_registro(LocalDateTime data_registro) {
		this.data_registro = data_registro;
	}

	public Integer getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(Integer id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getStr_senha() {
		return str_senha;
	}

	public void setStr_senha(String str_senha) {
		this.str_senha = str_senha;
	}

	public String getStr_nome() {
		return str_nome;
	}

	public void setStr_nome(String str_nome) {
		this.str_nome = str_nome;
	}

	public String getStr_cpf() {
		return str_cpf;
	}

	public void setStr_cpf(String str_cpf) {
		this.str_cpf = str_cpf;
	}

	public String getStr_email() {
		return str_email;
	}

	public void setStr_email(String str_email) {
		this.str_email = str_email;
	}

	public String getStr_telefone() {
		return str_telefone;
	}

	public void setStr_telefone(String str_telefone) {
		this.str_telefone = str_telefone;
	}

	public String getStr_endereco() {
		return str_endereco;
	}

	public void setStr_endereco(String str_endereco) {
		this.str_endereco = str_endereco;
	}

	public String getStr_cep() {
		return str_cep;
	}

	public void setStr_cep(String str_cep) {
		this.str_cep = str_cep;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDateTime getData_token() {
		return data_token;
	}

	public void setData_token(LocalDateTime data_token) {
		this.data_token = data_token;
	}

	public List<CompraDTO> getCompras() {
		return compras;
	}

	public void setCompras(List<CompraDTO> compras) {
		this.compras = compras;
	}

	public LocalDateTime getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(LocalDateTime data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

	public String getStr_logradouro() {
		return str_logradouro;
	}

	public void setStr_logradouro(String str_logradouro) {
		this.str_logradouro = str_logradouro;
	}

	public String getStr_complemento() {
		return str_complemento;
	}

	public void setStr_complemento(String str_complemento) {
		this.str_complemento = str_complemento;
	}

	public String getStr_bairro() {
		return str_bairro;
	}

	public void setStr_bairro(String str_bairro) {
		this.str_bairro = str_bairro;
	}

	public String getStr_localidade() {
		return str_localidade;
	}

	public void setStr_localidade(String str_localidade) {
		this.str_localidade = str_localidade;
	}

	public String getStr_uf() {
		return str_uf;
	}

	public void setStr_uf(String str_uf) {
		this.str_uf = str_uf;
	}

	public String getStr_unidade() {
		return str_unidade;
	}

	public void setStr_unidade(String str_unidade) {
		this.str_unidade = str_unidade;
	}
	
	
}
