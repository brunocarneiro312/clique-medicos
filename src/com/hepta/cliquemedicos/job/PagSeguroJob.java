package com.hepta.cliquemedicos.job;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hepta.cliquemedicos.entity.Servico;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.xml.XmlMapper;
import com.hepta.cliquemedicos.dto.AssociadosDTO;
import com.hepta.cliquemedicos.dto.CamposEmailDTO;
import com.hepta.cliquemedicos.dto.CompraDTO;
import com.hepta.cliquemedicos.dto.EspecialidadeDTO;
import com.hepta.cliquemedicos.dto.enums.EstadoCompraEnum;
import com.hepta.cliquemedicos.dto.enums.TipoEmailEnum;
import com.hepta.cliquemedicos.dto.pagseguro.TransactionDTO;
import com.hepta.cliquemedicos.dto.pagseguro.TransactionSearchResultDTO;
import com.hepta.cliquemedicos.entity.Compra;
import com.hepta.cliquemedicos.persistence.CompraDAO;
import com.hepta.cliquemedicos.persistence.SuperDAO;
import com.hepta.cliquemedicos.util.AmbienteUtil;
import com.hepta.cliquemedicos.util.EmailUtil;

public class PagSeguroJob implements Job {
	private Client client = ClientBuilder.newClient();

	public PagSeguroJob() {
		super();
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			this.rodaJob();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro no job de tentar atualizar estados de compras no pagseguro.");
		}
	}
	
	public void rodaJob() throws Exception {
		CompraDAO dao = new CompraDAO();
		List<Compra> comprasNaoPagas = dao.getNaoPagos();
		if (comprasNaoPagas.isEmpty())
			return;
		TransactionSearchResultDTO comprasPagSeguro = checarStatusCompras(comprasNaoPagas);
		List<List<TransactionDTO>> canceladosEPagos = atualizaEstados(comprasPagSeguro,comprasNaoPagas);
		enviarEmails(canceladosEPagos);
	}

	public List<List<TransactionDTO>> atualizaEstados(TransactionSearchResultDTO comprasPagSeguro, List<Compra>comprasNaoPagas) throws Exception {
		//TODO: ver casos de grande quantidade de produtos
		//		List<TransactionDTO> transacoes = new ArrayList<>();
		//		Integer totalPaginas = Integer.parseInt(comprasPagSeguro.getTotalPages());
		//		for (int i = 0; i < totalPaginas; i++) {
		//			
		//		}
		CompraDAO dao = new CompraDAO();
		List<TransactionDTO> transacoesPagas = new ArrayList<>();
		List<TransactionDTO> transacoesAguardando = new ArrayList<>();
		List<TransactionDTO> transacoesCanceladas = new ArrayList<>();
		List<TransactionDTO> transacoesEmAnalise = new ArrayList<>();
		for (TransactionDTO t : comprasPagSeguro.getTransactions()) {
			for (Compra c : comprasNaoPagas) {
				if(t.getCode().equals(c.getStr_transaction_code())) {
					EstadoCompraEnum estado = EstadoCompraEnum.fromInt(Integer.parseInt(t.getStatus())); // TODO: andressa.valadares (adicionar novos estados ao Enum)
					switch (estado) {
						case AGUARDANDO_PAGAMENTO:
							transacoesAguardando.add(t);
							break;
						case CANCELADA:
							transacoesCanceladas.add(t);
							break;
						case EM_ANALISE:
							transacoesEmAnalise.add(t);
							break;
						case PAGO:
							transacoesPagas.add(t);
							break;
						default:
							break;
					}
					break;
				}
			}
		}

		dao.atualizaEstado(EstadoCompraEnum.CANCELADA, transacoesCanceladas);
		dao.atualizaEstado(EstadoCompraEnum.EM_ANALISE, transacoesEmAnalise);
		dao.atualizaEstado(EstadoCompraEnum.PAGO, transacoesPagas);

		List<List<TransactionDTO>> canceladosEPagos = new ArrayList<>();
		canceladosEPagos.add(transacoesPagas);
		canceladosEPagos.add(transacoesCanceladas);
		return canceladosEPagos;
	}

	public TransactionSearchResultDTO checarStatusCompras(List<Compra> comprasNaoPagas) {
		try {
			List<LocalDateTime> datas = new ArrayList<>();
			LocalDateTime hoje = LocalDateTime.now();
			hoje.truncatedTo(ChronoUnit.DAYS);
			LocalDateTime ontem = hoje.minusDays(20);
			for (Compra c : comprasNaoPagas) {
				LocalDateTime data = c.getDate_registro();
				if (data.getMonthValue() == hoje.getMonthValue()) {
					if (data.isAfter(ontem))
						datas.add(c.getDate_registro());
				}
				//TODO: o que fazer caso a compra passar do tempo limite de espera?
			}
			Response responsePS = client.target(AmbienteUtil.ENDERECO_PAG_SEGURO).path("transactions")
					.queryParam("email", AmbienteUtil.EMAIL_PAG_SEGURO)
					.queryParam("token", AmbienteUtil.TOKEN_PAG_SEGURO).queryParam("initialDate", ontem)
					.queryParam("finalDate", LocalDateTime.now()).request().get();

			String aux = responsePS.readEntity(String.class);
			//System.out.println(aux);

			XmlMapper xmlMapper = new XmlMapper();
			return xmlMapper.readValue(aux, TransactionSearchResultDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void enviarEmails(List<List<TransactionDTO>> canceladosEPagos) throws Exception {
		CompraDAO dao = new CompraDAO();
		List<Compra> comprasPagas = dao.getServicoECompras(canceladosEPagos.get(0));
		List<Compra> comprasCanceladas = dao.getServicoECompras(canceladosEPagos.get(1));
		for (int i = 0; i < comprasPagas.size(); i++) {
			Compra aux = comprasPagas.get(i);
			CamposEmailDTO campos = new CamposEmailDTO();
			//seta dados provinientes do AMHP
			CompraDTO c = new CompraDTO(aux);
			AssociadosDTO associado = getAssociadoAMHP(c.getStr_especialidade(), c.getStr_matricula_credenciado(), c.getStr_matricula_prestador(),  c.getStr_associado_endereco());
			EspecialidadeDTO especialidade = getEspecialidadeAMHP(c.getStr_especialidade());
			
			c.setStr_associado_endereco(associado.getEndereco() +", "+ associado.getBairrodesc());
			c.setStr_especialidade(especialidade.getDescricao());
			
			campos.prepararEmailConfirmacaoCompra(aux.getServico(), c, associado.getNomeCredenciado());
			
			EmailUtil.enviarEmail(campos, aux.getLong_voucher().toString(), TipoEmailEnum.AGENDAMENTO_PAGO);
		}
		for (int i = 0; i < comprasCanceladas.size(); i++) {
			Compra aux = comprasCanceladas.get(i);
			CamposEmailDTO campos = new CamposEmailDTO();
			//seta dados provinientes do AMHP
			CompraDTO c = new CompraDTO(aux);
			AssociadosDTO associado = getAssociadoAMHP(c.getStr_especialidade(), c.getStr_matricula_credenciado(), c.getStr_matricula_prestador(),  c.getStr_associado_endereco());
			EspecialidadeDTO especialidade = getEspecialidadeAMHP(c.getStr_especialidade());
			
			c.setStr_associado_endereco(associado.getEndereco() +", "+ associado.getBairrodesc());
			c.setStr_especialidade(especialidade.getDescricao());
			//TODO melhorar essa busca PFVR
			campos.prepararEmailConfirmacaoCompra(aux.getServico(), c, associado.getNomeCredenciado());
			System.out.println("CANCELADO");
			EmailUtil.enviarEmail(campos, "", TipoEmailEnum.AGENDAMENTO_CANCELADO);
			EmailUtil.enviarEmail(campos, "", TipoEmailEnum.AGENDAMENTO_ASSOCIADO_CANCELADO);

			// TODO verificar se existe um agendamento ou se é um serviço não agendável
			Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/AgendaConsulta/" + aux.getServico().getId())
					.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY)
					.delete();

			if (responseAMHP.getStatus() == 200) {
				SuperDAO<Servico, Serializable> daoAgendamento = new SuperDAO<>(Servico.class);
				try {
					Servico servico = aux.getServico();
					servico.setId(null);
					daoAgendamento.update(servico);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(e);
				}
			}
		}
	}
	//TODO melhorar essa query
	private AssociadosDTO getAssociadoAMHP(String especialidadeId, String credenciado, String prestador, String pessoaEndereco) throws Exception{
		Response responseAMHP = client
				.target(AmbienteUtil.AHMP_REST_URI)
				.path("/Associados")
				.queryParam("especialidadeId", especialidadeId)
				.queryParam("credenciado", credenciado)
				.queryParam("prestador", prestador)
				.queryParam("pessoaEndereco", pessoaEndereco)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("api-key", AmbienteUtil.AHMP_API_KEY)
				.get();
		
		if(responseAMHP.getStatus() == 200)
			return responseAMHP.readEntity(AssociadosDTO.class);
		throw new Exception();
	}
	
	private EspecialidadeDTO getEspecialidadeAMHP(String especialidadeId) throws Exception{
		Response responseAMHP = client
				.target(AmbienteUtil.AHMP_REST_URI)
				.path("/Especialidades/" + especialidadeId)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("api-key", AmbienteUtil.AHMP_API_KEY)
				.get();
		
		if(responseAMHP.getStatus() == 200)
			return responseAMHP.readEntity(EspecialidadeDTO.class);
		throw new Exception();
	}

}
