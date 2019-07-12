package com.hepta.cliquemedicos.entity;

import com.hepta.cliquemedicos.dto.ServicoDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "Servico")
@Entity
public class Servico implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servico", nullable = false)
	private Integer id_servico;

	@Column(name = "id_amhp", nullable = true) //pode ser nulo no caso do boleto
	private Integer id; //id amhp

	@Column(name = "str_prestador")
	private String str_prestador;

	@Column(name = "str_credenciado")
	private String str_credenciado;

	@Column(name = "str_pessoa_endereco")
	private String str_pessoa_endereco; // Campo para diferenciar a localidade que o medico atende no AMHP

	@Column(name = "str_especialidade")
	private String str_especialidade;

	@Column(name = "str_nome_beneficiario")
	private String str_nome_beneficiario;

	@Column(name = "str_email_beneficiario")
	private String str_email_beneficiario;

	@Column(name = "str_telefone_beneficiario")
	private String str_telefone_beneficiario;
	
	@Column(name = "bool_ordem_chegada")
	private Boolean bool_ordem_chegada;

	@Column(name = "date_inicio")
	private LocalDateTime date_inicio;
	
	@OneToOne(mappedBy = "servico", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Compra compra;

	@Column(name = "bool_agendavel")
	private Boolean bool_agendavel;

	@Column(name = "id_servico_amhp") // Identificador do serviço na AMHP (ex: Pediatria, Hemograma, etc...)
	private String id_servico_amhp;

	public Servico() {
		super();
	}

	public Servico(ServicoDTO a) {
		//TODO
		//this.setStr_cpf_beneficiario(a.getMatriculaBeneficiario());
		this.setStr_nome_beneficiario(a.getNomeBeneficiario());
		this.setStr_email_beneficiario(a.getEmailBeneficiario());
		this.setStr_telefone_beneficiario(a.getTelefoneBeneficiario());
		this.setStr_prestador(a.getPrestador());
		this.setStr_credenciado(a.getCredenciado());
		this.setDate_inicio(LocalDateTime.parse(a.getInicio()));
		this.setStr_pessoa_endereco(a.getPessoaEndereco());
		this.setStr_especialidade(a.getEspecialidade());
		this.setBool_ordem_chegada(a.getOrdemDeChegada());
	}

	
	public Boolean getBool_ordem_chegada() {
		return bool_ordem_chegada;
	}

	public void setBool_ordem_chegada(Boolean bool_ordem_chegada) {
		this.bool_ordem_chegada = bool_ordem_chegada;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Integer getId_servico() {
		return id_servico;
	}

	public void setId_servico(Integer id_agendamento) {
		this.id_servico = id_agendamento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStr_prestador() {
		return str_prestador;
	}

	public void setStr_prestador(String str_prestador) {
		this.str_prestador = str_prestador;
	}

	public String getStr_credenciado() {
		return str_credenciado;
	}

	public void setStr_credenciado(String str_credenciado) {
		this.str_credenciado = str_credenciado;
	}

	public String getStr_pessoa_endereco() {
		return str_pessoa_endereco;
	}

	public void setStr_pessoa_endereco(String str_pessoa_endereco) {
		this.str_pessoa_endereco = str_pessoa_endereco;
	}

	public String getStr_especialidade() {
		return str_especialidade;
	}

	public void setStr_especialidade(String str_especialidade) {
		this.str_especialidade = str_especialidade;
	}

	public String getStr_nome_beneficiario() {
		return str_nome_beneficiario;
	}

	public void setStr_nome_beneficiario(String str_nome_beneficiario) {
		this.str_nome_beneficiario = str_nome_beneficiario;
	}

	public String getStr_email_beneficiario() {
		return str_email_beneficiario;
	}

	public void setStr_email_beneficiario(String str_email_beneficiario) {
		this.str_email_beneficiario = str_email_beneficiario;
	}

	public String getStr_telefone_beneficiario() {
		return str_telefone_beneficiario;
	}

	public void setStr_telefone_beneficiario(String str_telefone_beneficiario) {
		this.str_telefone_beneficiario = str_telefone_beneficiario;
	}

	public LocalDateTime getDate_inicio() {
		return date_inicio;
	}

	public void setDate_inicio(LocalDateTime date_inicio) {
		this.date_inicio = date_inicio;
	}

	public Boolean getBool_agendavel() {
		return bool_agendavel;
	}

	public void setBool_agendavel(Boolean bool_agendavel) {
		this.bool_agendavel = bool_agendavel;
	}

	public String getId_servico_amhp() {
		return id_servico_amhp;
	}

	public void setId_servico_amhp(String id_servico_amhp) {
		this.id_servico_amhp = id_servico_amhp;
	}
}
