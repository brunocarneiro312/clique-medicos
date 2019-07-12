package com.hepta.cliquemedicos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hepta.cliquemedicos.entity.Servico;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CamposEmailDTO {
	private String numeroVoucher;
	private String dataCompra;
	private String valor;
	private Integer id; // ID AMHP
	private Integer idAgendamento; // ID CliqueMedicos

	private String especialidade;
	private String credenciado;
	private String nomeCredenciado;
	private String prestador;
	private String prestadorNome;
	private String prestadorEmail;
	private String enderecoAtendimento;
	private String inicio;
	private String fim;
	@JsonProperty("pessoaEndereco")
	private String associadoEndereco; // Campo para diferenciar a localidade que o medico atende no AMHP
	private String especialidadeDescricao;
	private String nomeBeneficiario;
	private String matriculaBeneficiario;
	private String emailBeneficiario;
	private String telefoneBeneficiario;

	// Viabiliza a passagem de parâmetros adicionais para o template
	private Map<String, Object> parametrosAdicionais = new HashMap<String, Object>();

	public String getDataEmail() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm");
		LocalDateTime data = LocalDateTime.parse(inicio);
		return formatter.format(data.plusHours(-3));
	}

	public String getDataCompraEmail() {
		String resultado = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm");
			LocalDateTime data = LocalDateTime.parse(dataCompra);
			resultado = formatter.format(data);
		} catch(Exception e) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			ZonedDateTime data = ZonedDateTime.parse(dataCompra);
			resultado = formatter.format(data);
		}
		return resultado;
	}

	public CamposEmailDTO() {
		super();
	}

	public CamposEmailDTO(CamposEmailDTO a) {
		super();
		this.setMatriculaBeneficiario(a.getMatriculaBeneficiario());
		this.setCredenciado(a.getCredenciado());
		this.setDataCompra(a.getDataCompra());
		this.setEmailBeneficiario(a.getEmailBeneficiario());
		this.setEnderecoAtendimento(a.getEnderecoAtendimento());
		this.setEspecialidade(a.getEspecialidade());
		this.setEspecialidadeDescricao(a.getEspecialidadeDescricao());
		this.setFim(a.getFim());
		this.setId(a.getId());
		this.setInicio(a.getInicio());
		this.setNomeBeneficiario(a.getNomeBeneficiario());
		this.setNomeCredenciado(a.getNomeCredenciado());
		this.setNumeroVoucher(a.getNumeroVoucher());
		this.setAssociadoEndereco(a.getAssociadoEndereco());
		this.setPrestador(a.getPrestador());
		this.setPrestadorEmail(a.getPrestadorEmail());
		this.setPrestadorNome(a.getPrestadorNome());
		this.setTelefoneBeneficiario(a.getTelefoneBeneficiario());
		this.setValor(a.getValor());
		this.setIdAgendamento(a.getIdAgendamento());
	}

	public Integer getIdAgendamento() {
		return idAgendamento;
	}

	public void setIdAgendamento(Integer idAgendamento) {
		this.idAgendamento = idAgendamento;
	}

	public String getNumeroVoucher() {
		return numeroVoucher;
	}

	public void setNumeroVoucher(String numeroVoucher) {
		this.numeroVoucher = numeroVoucher;
	}

	public String getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getCredenciado() {
		return credenciado;
	}

	public void setCredenciado(String credenciado) {
		this.credenciado = credenciado;
	}

	public String getNomeCredenciado() {
		return nomeCredenciado;
	}

	public void setNomeCredenciado(String nomeCredenciado) {
		this.nomeCredenciado = nomeCredenciado;
	}

	public String getPrestador() {
		return prestador;
	}

	public void setPrestador(String prestador) {
		this.prestador = prestador;
	}

	public String getPrestadorNome() {
		return prestadorNome;
	}

	public void setPrestadorNome(String prestadorNome) {
		this.prestadorNome = prestadorNome;
	}

	public String getPrestadorEmail() {
		return prestadorEmail;
	}

	public void setPrestadorEmail(String prestadorEmail) {
		this.prestadorEmail = prestadorEmail;
	}

	public String getEnderecoAtendimento() {
		return enderecoAtendimento;
	}

	public void setEnderecoAtendimento(String enderecoAtendimento) {
		this.enderecoAtendimento = enderecoAtendimento;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public String getAssociadoEndereco() {
		return associadoEndereco;
	}

	public void setAssociadoEndereco(String associadoEndereco) {
		this.associadoEndereco = associadoEndereco;
	}

	public String getEspecialidadeDescricao() {
		return especialidadeDescricao;
	}

	public void setEspecialidadeDescricao(String especialidadeDescricao) {
		this.especialidadeDescricao = especialidadeDescricao;
	}

	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}

	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public String getMatriculaBeneficiario() {
		return matriculaBeneficiario;
	}

	public void setMatriculaBeneficiario(String matriculaBeneficiario) {
		this.matriculaBeneficiario = matriculaBeneficiario;
	}

	public String getEmailBeneficiario() {
		return emailBeneficiario;
	}

	public void setEmailBeneficiario(String emailBeneficiario) {
		this.emailBeneficiario = emailBeneficiario;
	}

	public String getTelefoneBeneficiario() {
		return telefoneBeneficiario;
	}

	public void setTelefoneBeneficiario(String telefoneBeneficiario) {
		this.telefoneBeneficiario = telefoneBeneficiario;
	}

	public void prepararEmailConfirmacaoCompra(Servico a, CompraDTO c, String nomeCredenciado) {
		//TODO
		//this.setMatriculaBeneficiario(a.getCompra().getUsuario().getId_usuario().toString());
		this.setTelefoneBeneficiario(a.getStr_telefone_beneficiario());
		this.setEmailBeneficiario(a.getStr_email_beneficiario());
		this.setIdAgendamento(a.getId_servico());
		this.setNomeBeneficiario(a.getStr_nome_beneficiario());
		this.setValor(c.getDec_valor_consulta().toString());
		this.setInicio(a.getDate_inicio().toString());
		this.setId(a.getId());
		
		this.setEnderecoAtendimento(c.getStr_associado_endereco());
		this.setDataCompra(c.getDate_registro().toString());
		this.setEspecialidadeDescricao(c.getStr_especialidade());
		this.setNumeroVoucher(c.getLong_voucher().toString());
		this.setEspecialidade(a.getStr_especialidade());
		this.setPrestador(a.getStr_prestador());
		this.setPrestadorNome(c.getStr_nome_associado());
		this.setNomeCredenciado(nomeCredenciado);
		this.setCredenciado(a.getStr_credenciado());
		this.setPrestadorEmail("cliquemedicos@amhp.com.br");//TODO: MUDAR PARA O E-MAIL DO ASSOCIAD
		
	}

	public Map<String, Object> getParametrosAdicionais() {
		return parametrosAdicionais;
	}

	public void setParametrosAdicionais(Map<String, Object> parametrosAdicionais) {
		this.parametrosAdicionais = parametrosAdicionais;
	}
}
