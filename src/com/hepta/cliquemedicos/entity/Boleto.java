package com.hepta.cliquemedicos.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.hepta.cliquemedicos.dto.MensagemBoletoDTO;

@Table(name = "Boleto")
@Entity
public class Boleto {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_boleto", nullable = false)
	private Integer id_boleto;

	@Column(name = "id_amhp", nullable = false) //id do boleto no AMHP
	private Integer id_amhp;

	@Column(name = "str_status")
	private String str_status;
	
	@Column(name = "str_url") //TODO verificar se a URL expira, é meio estranho salvar url no BD
	private String str_url;
	
	@Column(name = "str_status_descricao")
	private String str_status_descricao;

	@Column(name = "str_codigo_barras")
	private String str_codigo_barras;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_criacao")
	private Date date_criacao;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_modificacao")
	private Date date_modificacao;

	@OneToOne(mappedBy = "boleto")
	private Compra compra;
	
	public Boleto() {
		
	}
	
	public Boleto(MensagemBoletoDTO a) {
		this.setId_boleto(a.getIdBoleto());
		this.setId_amhp(a.getIdAMHP());
		this.setStr_status(a.getStatus());
		this.setStr_status_descricao(a.getStatusDescricao());
		this.setStr_codigo_barras(a.getCodigoBarras());
		this.setStr_url(a.getUrlBoleto());
	}
	
	public Date getDate_criacao() {
		return date_criacao;
	}

	public void setDate_criacao(Date date_criacao) {
		this.date_criacao = date_criacao;
	}

	public Date getDate_modificacao() {
		return date_modificacao;
	}

	public void setDate_modificacao(Date date_modificacao) {
		this.date_modificacao = date_modificacao;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public String getStr_status_descricao() {
		return str_status_descricao;
	}

	public String getStr_url() {
		return str_url;
	}

	public void setStr_url(String str_url) {
		this.str_url = str_url;
	}

	public void setStr_status_descricao(String str_status_descricao) {
		this.str_status_descricao = str_status_descricao;
	}

	public Integer getId_boleto() {
		return id_boleto;
	}

	public void setId_boleto(Integer id_boleto) {
		this.id_boleto = id_boleto;
	}

	public Integer getId_amhp() {
		return id_amhp;
	}

	public void setId_amhp(Integer id_amhp) {
		this.id_amhp = id_amhp;
	}

	public String getStr_status() {
		return str_status;
	}

	public void setStr_status(String str_status) {
		this.str_status = str_status;
	}

	public String getStr_codigo_barras() {
		return str_codigo_barras;
	}

	public void setStr_codigo_barras(String str_codigo_barras) {
		this.str_codigo_barras = str_codigo_barras;
	}
	
}
