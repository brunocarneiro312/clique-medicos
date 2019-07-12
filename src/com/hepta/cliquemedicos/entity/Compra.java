package com.hepta.cliquemedicos.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hepta.cliquemedicos.dto.CompraDTO;

@Table(name = "Compra")
@Entity
public class Compra implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_compra", nullable = false)
	private Integer id_compra;

	@OneToOne(fetch = FetchType.LAZY)// TODO usar um cascade persist, talvez?
	@JoinColumn(name = "id_servico")
	private Servico servico;

	@Column(name = "str_matricula_prestador")
	private String str_matricula_prestador;

	@Column(name = "str_matricula_credenciado")
	private String str_matricula_credenciado;

	@Column(name = "long_voucher", unique = true)
	private Long long_voucher;

	@Column(name = "str_especialidade")
	private String str_especialidade;

	@Column(name = "str_nome_associado")
	private String str_nome_associado;

	@Column(name = "str_associado_endereco")
	private String str_associado_endereco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "date_registro")
	private LocalDateTime date_registro;

	@Column(name = "date_consulta")
	private LocalDateTime date_consulta;

	@Column(name = "dec_valor_consulta")
	private BigDecimal dec_valor_consulta;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_boleto", nullable = true)
	private Boleto boleto;

	@Column(name = "int_estado_compra")
	private Integer int_estado_compra;
	
	@Column(name = "str_transaction_code") // Codigo de pagamento do pagseguro
	private String str_transaction_code;

	public Compra() {
		super();
	}

	public Compra(CompraDTO compraDTO, Integer idServico) {
		
		Servico servico = new Servico();
		servico.setId_servico(idServico);
		
		if (compraDTO != null) {
			if (compraDTO.getId_compra() != null && compraDTO.getId_compra() < 0) {				
				this.setId_compra(null);
			}
			else {
				this.setId_compra(compraDTO.getId_compra());
			}
		}
		
		this.setUsuario                  (new Usuario(compraDTO.getUsuario()));
		this.setDate_consulta            (compraDTO.getDate_consulta());
		this.setDate_registro            (compraDTO.getDate_registro());
		this.setDec_valor_consulta       (compraDTO.getDec_valor_consulta());
		this.setId_compra                (compraDTO.getId_compra());
		this.setLong_voucher             (compraDTO.getLong_voucher());
		this.setStr_especialidade        (compraDTO.getStr_especialidade());
		this.setStr_matricula_credenciado(compraDTO.getStr_matricula_credenciado());
		this.setStr_matricula_prestador  (compraDTO.getStr_matricula_prestador());
		this.setStr_nome_associado       (compraDTO.getStr_nome_associado());
		this.setStr_especialidade        (compraDTO.getStr_especialidade());
		this.setInt_estado_compra        (compraDTO.getInt_estado_compra());
		this.setStr_associado_endereco   (compraDTO.getStr_associado_endereco());
		this.setServico(servico);
		
		if(compraDTO.getBoleto() != null) {			
			this.setBoleto(new Boleto(compraDTO.getBoleto()));
		}
		else {
			this.setBoleto(null);
		}
		
		this.setStr_transaction_code(compraDTO.getStr_transaction_code());
	}

	public Compra(CompraDTO a) {
		super();
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
		this.setStr_associado_endereco(a.getStr_associado_endereco());
		this.setInt_estado_compra(a.getInt_estado_compra());
		this.setServico(new Servico(a.getServico()));
		this.setBoleto(new Boleto(a.getBoleto()));
		this.setStr_transaction_code(a.getStr_transaction_code());
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public Servico getServico() {
		return servico;
	}

	public String getStr_transaction_code() {
		return str_transaction_code;
	}

	public void setStr_transaction_code(String str_transaction_code) {
		this.str_transaction_code = str_transaction_code;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public Integer getInt_estado_compra() {
		return int_estado_compra;
	}

	public void setInt_estado_compra(Integer int_estado_compra) {
		this.int_estado_compra = int_estado_compra;
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

	public Integer getId_compra() {
		return id_compra;
	}

	public void setId_compra(Integer id_compra) {
		this.id_compra = id_compra;
	}

	public Long getLong_voucher() {
		return long_voucher;
	}

	public void setLong_voucher(Long long_voucher) {
		this.long_voucher = long_voucher;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getStr_associado_endereco() {
		return str_associado_endereco;
	}

	public void setStr_associado_endereco(String str_associado_endereco) {
		this.str_associado_endereco = str_associado_endereco;
	}
}
