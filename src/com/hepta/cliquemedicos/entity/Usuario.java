package com.hepta.cliquemedicos.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hepta.cliquemedicos.dto.CompraDTO;
import com.hepta.cliquemedicos.dto.UsuarioDTO;

@Table(name = "Usuario")
@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario", nullable = false)
	private Integer id_usuario;

	@Column(name = "str_senha")
	private String str_senha;

	@Column(name = "str_nome")
	private String str_nome;

	@Column(name = "str_cpf")
	private String str_cpf;

	@Column(name = "str_email")
	private String str_email;

	@Column(name = "str_telefone")
	private String str_telefone;

	@Column(name = "str_endereco")
	private String str_endereco;

	@Column(name = "str_cep")
	private String str_cep;

	@Column(name = "str_logradouro")
	private String str_logradouro;
	
	@Column(name = "str_complemento")
	private String str_complemento;
	
	@Column(name = "str_bairro")
	private String str_bairro;  
	
	@Column(name = "str_localidade")
	private String str_localidade;
	
	@Column(name = "str_uf")
	private String str_uf;
	
	@Column(name = "str_unidade")
	private String str_unidade;   
	
	@Column(name = "str_token_verificacao")
	private String str_token_verificacao;
	
	@Column(name = "data_token")
	private LocalDateTime data_token;

	@Column(name = "bool_verificado")
	private Boolean bool_verificado;
	
	@Column(name = "data_nascimento")
	private LocalDateTime data_nascimento;

	@Column(name = "data_registro")
	private LocalDateTime data_registro;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.MERGE })
	private List<Compra> compras = new ArrayList<Compra>();

	public Usuario() {
		super();
	}

	public Usuario(UsuarioDTO usuarioDTO) {
		
		super();
		
		this.setId_usuario           (usuarioDTO.getId_usuario());
		this.setStr_cep              (usuarioDTO.getStr_cep());
		this.setStr_logradouro       (usuarioDTO.getStr_logradouro());
		this.setStr_complemento      (usuarioDTO.getStr_complemento());
		this.setStr_bairro           (usuarioDTO.getStr_bairro()); 
		this.setStr_localidade       (usuarioDTO.getStr_localidade());
		this.setStr_uf               (usuarioDTO.getStr_uf());
		this.setStr_unidade          (usuarioDTO.getStr_unidade());   
		this.setStr_cpf              (usuarioDTO.getStr_cpf());
		this.setStr_email            (usuarioDTO.getStr_email());
		this.setStr_endereco         (usuarioDTO.getStr_endereco());
		this.setStr_nome             (usuarioDTO.getStr_nome());
		this.setStr_telefone         (usuarioDTO.getStr_telefone());
		this.setData_registro        (usuarioDTO.getData_registro());
		this.setBool_verificado      (usuarioDTO.getBool_verificado());
		this.setStr_token_verificacao(usuarioDTO.getStr_token_verificacao());
		this.setData_token           (usuarioDTO.getData_token());
		this.setData_nascimento      (usuarioDTO.getData_nascimento());
		
		List<Compra> list = new ArrayList<>();
		
		for (CompraDTO compraDTO : usuarioDTO.getCompras()) {
			list.add(new Compra(compraDTO));
		}
		
		this.setCompras(list);
		//this.setStr_senha(a.getStr_senha());
	}

	public Usuario(Usuario a) {
		super();
		this.setId_usuario(a.getId_usuario());
		this.setStr_cep(a.getStr_cep());
		this.setStr_cpf(a.getStr_cpf());
		this.setStr_email(a.getStr_email());
		this.setStr_endereco(a.getStr_endereco());
		this.setStr_nome(a.getStr_nome());
		//this.setStr_senha(a.getStr_senha());
		this.setStr_telefone(a.getStr_telefone());
		this.setData_registro(a.getData_registro());
		this.setBool_verificado(a.getBool_verificado());
		this.setStr_token_verificacao(a.getStr_token_verificacao());
		this.setData_token(a.getData_token());
		this.setCompras(a.getCompras());
		this.setData_nascimento(a.getData_nascimento());
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
		if(this.getStr_nome() == null
		|| this.getStr_cpf() == null
		|| this.getStr_email() == null
		//|| this.getStr_endereco() == null
		//|| this.getStr_cep() == null
		|| this.getStr_senha() == null
		|| this.getStr_telefone() == null
		|| this.getData_nascimento() == null
		//|| this.getStr_cep().isEmpty()
		//|| this.getStr_endereco().isEmpty()
		|| this.getStr_cpf().isEmpty()
		|| this.getStr_email().isEmpty()
		|| this.getStr_nome().isEmpty()
		|| this.getStr_senha().isEmpty()
		|| this.getStr_telefone().isEmpty())
			return false;
		else
			return true;
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

	public LocalDateTime getData_token() {
		return data_token;
	}

	public void setData_token(LocalDateTime data_token) {
		this.data_token = data_token;
	}

	public List<Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<Compra> compras) {
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
