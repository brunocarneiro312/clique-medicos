package com.hepta.cliquemedicos.job;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hepta.cliquemedicos.dto.*;
import com.hepta.cliquemedicos.entity.Servico;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.hepta.cliquemedicos.dto.ServicoDTO;
import com.hepta.cliquemedicos.dto.enums.EstadoBoletoEnum;
import com.hepta.cliquemedicos.dto.enums.EstadoCompraEnum;
import com.hepta.cliquemedicos.dto.enums.TipoEmailEnum;
import com.hepta.cliquemedicos.entity.Compra;
import com.hepta.cliquemedicos.persistence.ServicoDAO;
import com.hepta.cliquemedicos.persistence.CompraDAO;
import com.hepta.cliquemedicos.persistence.SuperDAO;
import com.hepta.cliquemedicos.util.AmbienteUtil;
import com.hepta.cliquemedicos.util.EmailUtil;

public class BoletoJob implements Job {
	private Client client = ClientBuilder.newClient();

	public BoletoJob() {
		super();
	}
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			this.rodaJob();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro no job de tentar atualizar estados de compras efetuadas com boleto.");
		}
	}
		
	public void rodaJob() throws Exception {
		CompraDAO dao = new CompraDAO();
		// Carrega todos os boletos, com as compras e agendamentos
		List<Compra> boletosAguardando = dao.getBoletosAguardandoPagamento();
		HashMap<Integer, Compra> mapaBoletosAguardando = new HashMap<Integer, Compra>();
		if (boletosAguardando.isEmpty())
			return;
		
		//Salva todos os boletos que podem ser atualizados no hashmap
		for(Compra compra :  boletosAguardando) {
			mapaBoletosAguardando.put(compra.getBoleto().getId_amhp(), compra);
		}
		List<MensagemBoletoDTO> boletosAtualizados = checarStatusBoletos(); //traz os boletos que estão aguardando pagamento tbm
		
		if(boletosAtualizados.size() <= 0)
			return;//nao ha boletos atualizados
		
		List<List<Integer>> canceladosEPagos = atualizaEstados(boletosAtualizados, mapaBoletosAguardando);
		List<Integer> boletosCancelados =  canceladosEPagos.get(0);
		List<Integer> boletosPagos =  canceladosEPagos.get(1);
		if(boletosPagos.size() > 0)
			agendaBoletosPagos(boletosPagos, mapaBoletosAguardando);
		if(boletosCancelados.size() > 0)
			enviarEmailCancelados(boletosCancelados, mapaBoletosAguardando);
	}
	/*
	 * Para todos os boletos atualizados segundo o AMHP, atualiza os estados no BD e retorna uma lista 
	 * com os IDs do AMHP dos boletos pagos e cancelados */
	public List<List<Integer>> atualizaEstados(List<MensagemBoletoDTO> boletosAtualizados, 
			HashMap<Integer, Compra> mapaBoletosAguardando) throws Exception {
		
		CompraDAO dao = new CompraDAO();
		List<Integer> transacoesPagas = new ArrayList<>();
		List<Integer> transacoesCanceladas = new ArrayList<>();
		for (MensagemBoletoDTO atualizado : boletosAtualizados) {
			//Atualiza no BD apenas o boletos que estavam aguardando pagamento
			if(mapaBoletosAguardando.containsKey(atualizado.getIdAMHP())) {
				EstadoBoletoEnum estado = EstadoBoletoEnum.fromString(atualizado.getStatus());
				switch (estado) {
					case AGUARDANDO_PAGAMENTO:
						break;
					case CANCELADO:
						transacoesCanceladas.add(atualizado.getIdAMHP());
						break;
					case PAGO:
						transacoesPagas.add(atualizado.getIdAMHP());
						break;
					default:
						break;
				}
			}
		}
		
		Integer totalCancelado = dao.atualizaEstadoBoleto(EstadoCompraEnum.CANCELADA, transacoesCanceladas);
		System.out.println("Total de boletos cancelado:" + totalCancelado);
		Integer totalPago = dao.atualizaEstadoBoleto(EstadoCompraEnum.PAGO, transacoesPagas);
		System.out.println("Total de boletos pagos:" + totalPago);
		
		List<List<Integer>> canceladosEPagos = new ArrayList<>();
		canceladosEPagos.add(transacoesCanceladas);
		canceladosEPagos.add(transacoesPagas);
		return canceladosEPagos;
	}

	public List<MensagemBoletoDTO> checarStatusBoletos() {
		try {
			//TODO transformar em variaveis de ambiente (andressa.valadares)
			PeriodoBoletoDTO periodo = new PeriodoBoletoDTO();
			periodo.setInicio(AmbienteUtil.PERIODO_INICIAL_JOB.toString());
			periodo.setFim(AmbienteUtil.PERIODO_FINAL_JOB.toString());
			
			Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("Boleto/ObterPorPeriodo").request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY).post(Entity.json(periodo));
			
			if(responseAMHP.getStatus() == 200){
				List<MensagemBoletoDTO> boletosAtualizados=  responseAMHP.readEntity(new GenericType<List<MensagemBoletoDTO>>() {
				});
				return boletosAtualizados;
			}
			else{
				System.out.println(responseAMHP.readEntity(String.class));
				throw new Exception();
			}
			//MOCK
			//return getBoletosPorPeriodoMock();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<MensagemBoletoDTO> getBoletosPorPeriodoMock() {
		
		List<MensagemBoletoDTO> lista = new ArrayList<MensagemBoletoDTO>();
		MensagemBoletoDTO boleto = new MensagemBoletoDTO();
		boleto.setCodigoBarras("75691.50043  01000.032902  00036.910016  1  79320000010500");
		boleto.setIdAMHP(285);
		boleto.setStatus("P");
		boleto.setStatusDescricao("Pago");
		lista.add(boleto);
		
		MensagemBoletoDTO boleto2 = new MensagemBoletoDTO();
		boleto2.setCodigoBarras("75691.50043  01000.032902  00036.910016  1  79320000010500");
		boleto2.setIdAMHP(296);
		boleto2.setStatus("C");
		boleto2.setStatusDescricao("Cancelado");
		lista.add(boleto2);
		
		return lista;
	}
	
	private void agendaBoletosPagos(List<Integer> idsBoletosPagos,  HashMap<Integer, Compra> mapaBoletosAguardando) {
		for(Integer idAMHP : idsBoletosPagos) {
			if(mapaBoletosAguardando.containsKey(idAMHP)) {
				Compra	compraPaga = mapaBoletosAguardando.get(idAMHP);
				ServicoDTO agendamento = new ServicoDTO(compraPaga.getServico());
				CamposEmailDTO dadosAgendamentoConfirmado = null;
						
				try {
					dadosAgendamentoConfirmado = agendaHorario(agendamento);
					if(dadosAgendamentoConfirmado != null) {
						dadosAgendamentoConfirmado.setNomeBeneficiario(compraPaga.getServico().getStr_nome_beneficiario());
						dadosAgendamentoConfirmado.setEmailBeneficiario(compraPaga.getServico().getStr_email_beneficiario());
						dadosAgendamentoConfirmado.setInicio(compraPaga.getDate_consulta().toString());
						dadosAgendamentoConfirmado.setDataCompra(compraPaga.getDate_registro().toString());
						dadosAgendamentoConfirmado.setNumeroVoucher(compraPaga.getLong_voucher().toString());
						dadosAgendamentoConfirmado.setValor(compraPaga.getDec_valor_consulta().toString());

						EmailUtil.enviarEmail(dadosAgendamentoConfirmado, "", TipoEmailEnum.AGENDAMENTO_PAGO);
						EmailUtil.enviarEmail(dadosAgendamentoConfirmado, "", TipoEmailEnum.AGENDAMENTO_ASSOCIADO);
					} else {
						System.out.println("Não foi possível agendar");
						//Apaga agendamento da compra e da tabela de agendamento
						ServicoDAO servicoDAO = new ServicoDAO();
						servicoDAO.delete(compraPaga.getServico().getId_servico());
						// Envia email com o numero do voucher e dados do email
						System.out.println("Enviar email de 'não foi possivel agendar, toma seu vale compra aqui'");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
	}
	
	private CamposEmailDTO agendaHorario(final ServicoDTO servicoDTO) throws Exception {
		CamposEmailDTO agendamentoConfirmado = new CamposEmailDTO();

		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/AgendaConsulta").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY).post(Entity.json(servicoDTO));

		if (responseAMHP.getStatus() == 200) {
			Servico servico = new Servico(servicoDTO);
			SuperDAO<Servico, Serializable> dao = new SuperDAO<>(Servico.class);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			LocalDateTime hoje = LocalDateTime.now();

			agendamentoConfirmado = responseAMHP.readEntity(CamposEmailDTO.class);
			servico.setId(agendamentoConfirmado.getId());
			servico = dao.update(servico); //atualiza servico se tudo deu certo
			//agendamentoConfirmado.setDataCompra(hoje.format(formatter).toString());
			//TODO trocar para o email do associado em producao
			agendamentoConfirmado.setPrestadorEmail("cliquemedicos@amhp.com.br");//AMHP
			return agendamentoConfirmado;

		} else {
			String output = responseAMHP.readEntity(String.class);
			System.out.println("ERRO - CM:" + output);
			return null;
		}
	}
	
	private void enviarEmailCancelados(List<Integer> idsBoletosCancelados, HashMap<Integer, Compra> mapaBoletosAguardando) throws Exception {	
		List<Compra> comprasCanceladas = new ArrayList<Compra>();
		Set<String> especialidades = new HashSet<String>();
		// Cria lista com todos as comrpas cancealdas para buscar os dados do associado
		for(Integer idAMHP : idsBoletosCancelados) {
			if(mapaBoletosAguardando.containsKey(idAMHP)) {
				comprasCanceladas.add(mapaBoletosAguardando.get(idAMHP));
				especialidades.add(mapaBoletosAguardando.get(idAMHP).getStr_especialidade());
			}
		}
		// Cria array com os dados necessario para o request no AMHP
		List<RequestAssociadoDTO> requestLista = createArrayAssociado(comprasCanceladas);
		//Realiza o requests no AMHP
		List<AssociadosDTO> listaAssociados = getAssociadosAMHP(requestLista);
		//Cria map com idPessoaHorario e dados do associados
		Map<String, AssociadosDTO> mapAssociado = listaAssociados.stream()
				.collect(Collectors.toMap(AssociadosDTO::getPessoaEnderecoHorario, i -> i));
		
		//List<EspecialidadeDTO> listaEspecialidade = getEspecialidadeAMHP(especialidades);
		//Map<Integer, EspecialidadeDTO> mapEspecialidade = listaEspecialidade.stream().collect(Collectors.toMap(EspecialidadeDTO::getId, i -> i));
		
		for(Compra compraCancelada : comprasCanceladas) {	
				try {
					
					//seta dados provinientes do AMHP
					CompraDTO c = new CompraDTO(compraCancelada);
					AssociadosDTO associado = mapAssociado.get(compraCancelada.getStr_associado_endereco());
					CamposEmailDTO campos = new CamposEmailDTO();
					
					//seta dados provinientes do AMHP
					EspecialidadeDTO especialidade = getEspecialidadeAMHP(c.getStr_especialidade());
					
					c.setStr_associado_endereco(associado.getEndereco() +", "+ associado.getBairrodesc());
					c.setStr_especialidade(especialidade.getDescricao());
					c.setStr_nome_associado(associado.getNomePrestador());
					//TODO melhorar essa busca PFVR
					campos.prepararEmailConfirmacaoCompra(compraCancelada.getServico(), c, associado.getNomeCredenciado());
					System.out.println("CANCELADO");
					EmailUtil.enviarEmail(campos, "", TipoEmailEnum.AGENDAMENTO_CANCELADO);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}

	}
	
	private List<RequestAssociadoDTO> createArrayAssociado(List<Compra> compras) {
		List<RequestAssociadoDTO> listaRequestAssociados =  new ArrayList<RequestAssociadoDTO>();
		Set<RequestAssociadoDTO> set = new HashSet<>(listaRequestAssociados);
		
		for(Compra compra : compras) {
			RequestAssociadoDTO associado = new RequestAssociadoDTO(compra);
			set.add(associado);
		} 
		listaRequestAssociados.addAll(set);
		return listaRequestAssociados;

	}

	private List<AssociadosDTO> getAssociadosAMHP(List<RequestAssociadoDTO> associados) throws Exception {
		//ArrayList<Word> arr = new ArrayList<Word>(hw.values());
		Response responseAMHP = client
				.target(AmbienteUtil.AHMP_REST_URI)
				.path("/Associados/lista")
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("api-key", AmbienteUtil.AHMP_API_KEY)
				.post(Entity.json(associados));
		
		if(responseAMHP.getStatus() == 200) {
			List<AssociadosDTO> listaAssociados = responseAMHP.readEntity(new GenericType<List<AssociadosDTO>>() {
			});
			return listaAssociados;
		}
		return null;
			
		
	}
	
	/*private List<EspecialidadeDTO> getEspecialidadeAMHP(Set<String> especialidades) throws Exception{
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
		return null;
	}*/
	
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
