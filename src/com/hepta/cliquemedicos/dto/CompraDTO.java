package com.hepta.cliquemedicos.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hepta.cliquemedicos.dto.pagseguro.PaymentDTO;
import com.hepta.cliquemedicos.entity.Compra;
import com.hepta.cliquemedicos.util.LocalDateTimeDeserializer;

public class CompraDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id_compra;
	private ServicoDTO servico;
	private UsuarioDTO usuario;
	private String str_matricula_prestador; //TODO mudar para id depois?
	private String str_matricula_credenciado;
	private Long long_voucher;
	private String str_nome_associado; //TODO Podera ser removido depois
	private String str_associado_endereco; // Campo pessoaEndereco?
	private Boolean ordemDeChegada; //Usado para agendar um horário para ordem de chegada

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime date_registro;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime date_consulta;

	private BigDecimal dec_valor_consulta;
	private String str_especialidade;
	private Integer int_estado_compra;
	private PaymentDTO payment;
	private MensagemBoletoDTO boleto; //sera null caso seja cartao
	private String str_transaction_code;

	public CompraDTO() {
		super();
	}

	public CompraDTO(Compra a) {
		if (a != null) {
			if (a.getId_compra() != null && a.getId_compra() < 0)
				this.setId_compra(null);
			else
				this.setId_compra(a.getId_compra());
		}
		this.setDate_consulta(a.getDate_consulta());
		this.setDate_registro(a.getDate_registro());
		this.setDec_valor_consulta(a.getDec_valor_consulta());
		this.setId_compra(a.getId_compra());
		this.setLong_voucher(a.getLong_voucher());
		this.setStr_especialidade(a.getStr_especialidade());
		this.setStr_matricula_credenciado(a.getStr_matricula_credenciado());
		this.setStr_matricula_prestador(a.getStr_matricula_prestador());
		this.setStr_nome_associado(a.getStr_nome_associado());
		this.setStr_especialidade(a.getStr_especialidade());
		this.setInt_estado_compra(a.getInt_estado_compra());
		this.setStr_associado_endereco(a.getStr_associado_endereco());
		this.setPayment(null);
		if(a.getBoleto() != null && a.getBoleto().getId_boleto() != null)
			this.setBoleto(new MensagemBoletoDTO(a.getBoleto()));
		else
			this.setBoleto(null);
		this.setStr_transaction_code(a.getStr_transaction_code());
		//this.setUsuario(new UsuarioDTO(a.getUsuario()));
		//this.setServico(new ServicoDTO(a.getServico()));
	}

	public Boolean checkCampos() {
		if (this.getStr_especialidade() == null 
				|| this.getStr_matricula_credenciado() == null
				|| this.getStr_matricula_prestador() == null 
				|| this.getPayment() == null)
			return false;
		if (this.getStr_especialidade().isEmpty() 
				|| this.getStr_matricula_credenciado().isEmpty()
				|| this.getStr_matricula_prestador().isEmpty() 
				|| !this.getPayment().validarCampos())
			return false;
		return true;
	}
	
	public Boolean checkCamposSemPayment() {
		if (this.getStr_especialidade() == null 
				|| this.getStr_matricula_credenciado() == null
				|| this.getStr_matricula_prestador() == null)
			return false;
		if (this.getStr_especialidade().isEmpty() 
				|| this.getStr_matricula_credenciado().isEmpty()
				|| this.getStr_matricula_prestador().isEmpty())
			return false;
		return true;
	}


	public MensagemBoletoDTO getBoleto() {
		return boleto;
	}

	public void setBoleto(MensagemBoletoDTO boleto) {
		this.boleto = boleto;
	}

	public String getStr_transaction_code() {
		return str_transaction_code;
	}

	public void setStr_transaction_code(String str_transaction_code) {
		this.str_transaction_code = str_transaction_code;
	}

	public Boolean getOrdemDeChegada() {
		return ordemDeChegada;
	}

	public void setOrdemDeChegada(Boolean ordemDeChegada) {
		this.ordemDeChegada = ordemDeChegada;
	}

	public Integer getInt_estado_compra() {
		return int_estado_compra;
	}

	public void setInt_estado_compra(Integer int_estado_compra) {
		this.int_estado_compra = int_estado_compra;
	}

	public PaymentDTO getPayment() {
		return payment;
	}

	public void setPayment(PaymentDTO payment) {
		this.payment = payment;
	}

	public String getStr_especialidade() {
		return str_especialidade;
	}

	public void setStr_especialidade(String str_especialidade) {
		this.str_especialidade = str_especialidade;
	}

	public String getStr_nome_associado() {
		return str_nome_associado;
	}

	public void setStr_nome_associado(String str_nome_associado) {
		this.str_nome_associado = str_nome_associado;
	}

	public LocalDateTime getDate_consulta() {
		return date_consulta;
	}

	public void setDate_consulta(LocalDateTime date_consulta) {
		this.date_consulta = date_consulta;
	}

	public String getStr_matricula_prestador() {
		return str_matricula_prestador;
	}

	public void setStr_matricula_prestador(String str_matricula_prestador) {
		this.str_matricula_prestador = str_matricula_prestador;
	}

	public String getStr_matricula_credenciado() {
		return str_matricula_credenciado;
	}

	public void setStr_matricula_credenciado(String str_matricula_credenciado) {
		this.str_matricula_credenciado = str_matricula_credenciado;
	}

	public LocalDateTime getDate_registro() {
		return date_registro;
	}

	public void setDate_registro(LocalDateTime date_registro) {
		this.date_registro = date_registro;
	}

	public BigDecimal getDec_valor_consulta() {
		return dec_valor_consulta;
	}

	public void setDec_valor_consulta(BigDecimal dec_valor_consulta) {
		this.dec_valor_consulta = dec_valor_consulta;
	}

	public Long getLong_voucher() {
		return long_voucher;
	}

	public void setLong_voucher(Long long_voucher) {
		this.long_voucher = long_voucher;
	}

	public Integer getId_compra() {
		return id_compra;
	}

	public void setId_compra(Integer id_compra) {
		this.id_compra = id_compra;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ServicoDTO getServico() {
		return servico;
	}

	public void setServico(ServicoDTO servico) {
		this.servico = servico;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public String getStr_associado_endereco() {
		return str_associado_endereco;
	}

	public void setStr_associado_endereco(String str_associado_endereco) {
		this.str_associado_endereco = str_associado_endereco;
	}

}
