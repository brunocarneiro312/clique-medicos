package com.hepta.cliquemedicos.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.xml.XmlMapper;
import com.hepta.cliquemedicos.dto.ServicoDTO;
import com.hepta.cliquemedicos.dto.AssociadosDTO;
import com.hepta.cliquemedicos.dto.CamposEmailDTO;
import com.hepta.cliquemedicos.dto.CompraDTO;
import com.hepta.cliquemedicos.dto.EspecialidadeDTO;
import com.hepta.cliquemedicos.dto.MensagemBoletoDTO;
import com.hepta.cliquemedicos.dto.UsuarioDTO;
import com.hepta.cliquemedicos.dto.ValorConsultaDTO;
import com.hepta.cliquemedicos.dto.boleto.RequestBoletoDTO;
import com.hepta.cliquemedicos.dto.enums.EstadoCompraEnum;
import com.hepta.cliquemedicos.dto.enums.TipoEmailEnum;
import com.hepta.cliquemedicos.dto.pagseguro.AddressDTO;
import com.hepta.cliquemedicos.dto.pagseguro.BillingAddressDTO;
import com.hepta.cliquemedicos.dto.pagseguro.DocumentDTO;
import com.hepta.cliquemedicos.dto.pagseguro.InstallmentDTO;
import com.hepta.cliquemedicos.dto.pagseguro.ItemDTO;
import com.hepta.cliquemedicos.dto.pagseguro.PaymentDTO;
import com.hepta.cliquemedicos.dto.pagseguro.PhoneDTO;
import com.hepta.cliquemedicos.dto.pagseguro.SenderDTO;
import com.hepta.cliquemedicos.dto.pagseguro.SessionDTO;
import com.hepta.cliquemedicos.dto.pagseguro.ShippingDTO;
import com.hepta.cliquemedicos.dto.pagseguro.TransactionDTO;
import com.hepta.cliquemedicos.entity.Servico;
import com.hepta.cliquemedicos.entity.Compra;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.persistence.ServicoDAO;
import com.hepta.cliquemedicos.persistence.CompraDAO;
import com.hepta.cliquemedicos.persistence.SuperDAO;
import com.hepta.cliquemedicos.persistence.UsuarioDAO;
import com.hepta.cliquemedicos.security.Voucher;
import com.hepta.cliquemedicos.util.AmbienteUtil;
import com.hepta.cliquemedicos.util.EmailUtil;
import com.hepta.cliquemedicos.util.FormatadorCPF;
import com.hepta.cliquemedicos.util.ResourceUtil;

@Path("pagamento")
public class RESTPagamentoService {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	//private String API_KEY_NOSSA = "28236d8ec201df516d0f6472d516d72d";

	private Client client = ClientBuilder.newClient();

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@POST
	@Path("/private/boleto")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response compraBoleto(final CompraDTO compra) {
		
		// Setando a especialidade a ser consultada
		final String ESPECIALIDADE_ESCOLHIDA = compra.getStr_especialidade();
		
		// Obtendo dados da consulta
		Response responseObterValorConsulta = ResourceUtil.callObterValorConsulta(ESPECIALIDADE_ESCOLHIDA);
		
		// Obtendo o status do response
		int statusObterValorConsulta = responseObterValorConsulta.getStatus();
		
		// Obtendo DTO retornado da API
		ValorConsultaDTO valorConsulta = responseObterValorConsulta.readEntity(ValorConsultaDTO.class);
		
		// Caso tenha sido retornado com sucesso, segue processamento...
		if (statusObterValorConsulta == 200 && valorConsulta != null) {
			
			// Seta valor da consulta
			compra.setDec_valor_consulta(new BigDecimal(valorConsulta.getValor()));
			
			// Inicia processamento do retorno da API
			try {
				
				// Obtendo o usuário logado
				Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
				
				// Erro caso o usuário não esteja logado
				if (usuarioLogado == null) {
					return Response
							.status(Status.UNAUTHORIZED)
							.entity("Não autorizado")
							.build();
				}
				
				// Recuperando os dados adicionais do usuário logado
				UsuarioDAO usuarioDAO = new UsuarioDAO();
				usuarioLogado = usuarioDAO.find(usuarioLogado.getId_usuario());
				
				// Preparando dados para solicitar o boleto
				RequestBoletoDTO requestBoletoDTO = new RequestBoletoDTO();
				requestBoletoDTO.setCidade     (usuarioLogado.getStr_localidade());
				requestBoletoDTO.setUf         (usuarioLogado.getStr_uf());
				requestBoletoDTO.setBairro     (usuarioLogado.getStr_bairro());
				requestBoletoDTO.setNome       (usuarioLogado.getStr_nome());
				requestBoletoDTO.setCep        (usuarioLogado.getStr_cep());
				requestBoletoDTO.setCpfcnpj    (usuarioLogado.getStr_cpf());
				requestBoletoDTO.setEndereco   (usuarioLogado.getStr_endereco());
				requestBoletoDTO.setValorBoleto(compra.getDec_valor_consulta());
				
				// Solicitando geração de boleto para a API
				Response responseGerarBoleto = ResourceUtil.callGerarBoleto(requestBoletoDTO);
				
				// Obtém o status da requisição
				int statusGerarBoleto = responseGerarBoleto.getStatus();
				
				// Segue processamento caso o boleto tenha sido retornado
				if (statusGerarBoleto == 200) {
					
					// Obtém o DTO contendo o boleto
					MensagemBoletoDTO mensagemBoletoDTO = responseGerarBoleto.readEntity(MensagemBoletoDTO.class);
					
					if (mensagemBoletoDTO == null ) {
						return Response
								.status(Response.Status.INTERNAL_SERVER_ERROR)
								.entity("Erro durante a geração do boleto.")
								.build();
					}
					
					// Cria objeto de beneficiário
					UsuarioDTO beneficiario = compra.getUsuario();
					beneficiario.setId_usuario(usuarioLogado.getId_usuario());

					// Cria objeto de compra confirmada
					compra.setUsuario(beneficiario);

					// ===================================================================================================
					// TODO: bruno.carneiro
					// ===================================================================================================
					// verificar se pode agendar ou não

					// Salva servico e a compra  no banco de dados
					ServicoDTO servicoDTO = new ServicoDTO(compra);
					Servico servico = new Servico(servicoDTO);
					ServicoDAO servicoDAO = new ServicoDAO();
					servico = servicoDAO.save(servico);
					compra.setBoleto(mensagemBoletoDTO);
					compra.setInt_estado_compra(EstadoCompraEnum.AGUARDANDO_PAGAMENTO.getValor()); //boleto não foi pago ainda
					compra.setDate_registro(LocalDateTime.now()); //set a data de registro da compra
					Compra compraConfirmada = registraCompra(compra, servico.getId_servico());
					// ===================================================================================================

					// Obtém objeto contendo o corpo do email a ser enviado
					CamposEmailDTO campos = new CamposEmailDTO();
					//Busca associados e especialidade no AMHP
					Response  responseAssociado =  ResourceUtil.callGetAssociadoAMHP(compra.getStr_especialidade(), compra.getStr_matricula_credenciado(), compra.getStr_matricula_prestador(),  compra.getStr_associado_endereco());
					Response responseEspecialidade = ResourceUtil.callGetEspecialidadeAMHP(compra.getStr_especialidade());

					if(responseAssociado.getStatus() == 200 && responseEspecialidade.getStatus() == 200) {
						AssociadosDTO associado = responseAssociado.readEntity(AssociadosDTO.class);
						EspecialidadeDTO especialidade = responseEspecialidade.readEntity(EspecialidadeDTO.class);;
						CompraDTO compraConfirmadaDTO = new CompraDTO(compraConfirmada);

						compraConfirmadaDTO.setStr_associado_endereco(associado.getEndereco() +", "+ associado.getBairrodesc());
						compraConfirmadaDTO.setStr_especialidade(especialidade.getDescricao());
						compraConfirmadaDTO.setStr_nome_associado(associado.getNomePrestador());
						compraConfirmadaDTO.setLong_voucher(compraConfirmada.getLong_voucher());
						campos.setValor(String.valueOf(compraConfirmada.getDec_valor_consulta()));
						campos.setEmailBeneficiario(compraConfirmada.getUsuario().getStr_email());

						// Informando o número e o link do boleto pelos parâmetros adicionais
						Map<String, Object> parametrosAdicionais = new HashMap<String, Object>();
						parametrosAdicionais.put("numeroDoBoleto", mensagemBoletoDTO.getCodigoBarras().replaceAll("\\.", "").replaceAll("\\s", ""));
						parametrosAdicionais.put("linkDoBoleto", mensagemBoletoDTO.getUrlBoleto());
						campos.setParametrosAdicionais(parametrosAdicionais);

						campos.prepararEmailConfirmacaoCompra(servico, compraConfirmadaDTO, associado.getNomeCredenciado());


						// Envia e-mail
						EmailUtil.enviarEmail(campos, "", TipoEmailEnum.BOLETO);
					}
						
					
					// Retorna com sucesso
					return Response
							.ok()
							.entity(compraConfirmada)
							.build();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Retorna com erro 
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity("Erro durante a geração do boleto.")
				.build();
	}

	@POST
	@Path("/private/compraPS")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response comprarPagSeguro(final CompraDTO compra) {
		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/Procedimento/ObterValorConsulta")
				.queryParam("especialidadeId", compra.getStr_especialidade()).request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY).get();

		ValorConsultaDTO valorConsulta = responseAMHP.readEntity(ValorConsultaDTO.class);
		compra.setDec_valor_consulta(new BigDecimal(valorConsulta.getValor()));

		
		
		CamposEmailDTO agendamentoConfirmado = null;
		Compra compraConfirmada = new Compra();
		compra.setInt_estado_compra(EstadoCompraEnum.EM_ANALISE.getValor());

		Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
		if (usuarioLogado == null)
			return Response.status(Status.UNAUTHORIZED).entity("Não").build();
		UsuarioDTO beneficiario = compra.getUsuario();
		beneficiario.setId_usuario(usuarioLogado.getId_usuario());
		compra.setUsuario(beneficiario);
		ServicoDTO agendamento = new ServicoDTO(compra);

		if (!compra.checkCampos()) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Campos obrigatórios para a compra não foram preeenchidos").build();
		}
		try {
			agendamentoConfirmado = agendaHorario(agendamento);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST)
					.entity("Ops, alguém chegou primeiro. Esse horário não está mais disponível, agende outro horário!")
					.build();
		}
		if (agendamentoConfirmado == null) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Ops, alguém chegou primeiro. Esse horário não está mais disponível, agende outro horário!")
					.build();
		}

		PaymentDTO payment;

		try {
			payment = preparaCompraPagSeguro(compra.getPayment(), usuarioLogado, compra.getDec_valor_consulta());
			XmlMapper xmlMapper = new XmlMapper();
			String xml;
			xml = xmlMapper.writeValueAsString(payment);

			Response responsePS = client.target(AmbienteUtil.ENDERECO_PAG_SEGURO).path("transactions")
					.queryParam("email", AmbienteUtil.EMAIL_PAG_SEGURO)
					.queryParam("token", AmbienteUtil.TOKEN_PAG_SEGURO).request().post(Entity.xml(xml));

			// Pagamento realizado com sucesso
			if (responsePS.getStatus() == 200) {
				try {
					TransactionDTO transaction = responsePS.readEntity(TransactionDTO.class);
					compra.setInt_estado_compra(EstadoCompraEnum.EM_ANALISE.getValor());
					compra.setStr_nome_associado(agendamentoConfirmado.getPrestadorNome());
					compra.setStr_transaction_code(transaction.getCode());
					compraConfirmada = registraCompra(compra, agendamentoConfirmado.getIdAgendamento());

					agendamentoConfirmado.setNomeBeneficiario(compraConfirmada.getUsuario().getStr_nome());
					agendamentoConfirmado.setEmailBeneficiario(compraConfirmada.getUsuario().getStr_email());
					agendamentoConfirmado.setInicio(compraConfirmada.getDate_consulta().toString());
					agendamentoConfirmado.setDataCompra(transaction.getDate());
					agendamentoConfirmado.setNumeroVoucher(compraConfirmada.getLong_voucher().toString());
					agendamentoConfirmado.setValor(compraConfirmada.getDec_valor_consulta().toString());

					EmailUtil.enviarEmail(agendamentoConfirmado, "", TipoEmailEnum.AGENDAMENTO_CLIENTE);

					EmailUtil.enviarEmail(agendamentoConfirmado, "", TipoEmailEnum.AGENDAMENTO_ASSOCIADO);
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Seu agendamento foi realizado com sucesso, "
									+ "mas não foi possível enviar o email. Entre em contato CliqueMedicos pelo e-mail suporte@cliquemedicos.com.br")
							.build();
				}
				compraConfirmada.setLong_voucher(null);//não mandar o voucher pro usuário ainda, seria uma falha de segurança
				return Response.ok().entity(compraConfirmada).build();
			} else {
				System.out.println(responsePS.readEntity(String.class));
				try {
					cancelaHorario(agendamentoConfirmado.getId(), agendamentoConfirmado.getIdAgendamento());
					return Response.status(Status.BAD_REQUEST)
							.entity("Não foi possível efetuar o pagamento, verifique os seus dados.").build();
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
							"Erro ao registrar a compra, pagamento não foi realizado. Porém seu horário foi reservado, "
									+ "entre em contato CliqueMedicos pelo e-mail suporte@cliquemedicos.com.br")
							.build();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			cancelaHorario(agendamentoConfirmado.getId(), agendamentoConfirmado.getIdAgendamento());
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Erro ao registrar a compra, pagamento não foi realizado").build();
		}
	}
	@GET
	@Path("/private/sessaoPS")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response sessaoPagSeguro() {
		Response responsePS = client.target("https://ws.sandbox.pagseguro.uol.com.br/v2/").path("sessions")
				.queryParam("email", AmbienteUtil.EMAIL_PAG_SEGURO).queryParam("token", AmbienteUtil.TOKEN_PAG_SEGURO).request().post(null);
		//System.out.println(responseAMHP.readEntity(String.class));
		SessionDTO teste = responsePS.readEntity(SessionDTO.class);
		return Response.status(Status.OK).entity(teste.id).build();
	}

	@POST
	@Path("/private/voucher")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response comprarVoucher(final CompraDTO compra) {
		CamposEmailDTO agendamentoConfirmado = null;
		CompraDAO compraDAO = new CompraDAO();
		
		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/Procedimento/ObterValorConsulta")
				.queryParam("especialidadeId", compra.getStr_especialidade()).request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY).get();
		// Salva o valor da compra
		ValorConsultaDTO valorConsulta = responseAMHP.readEntity(ValorConsultaDTO.class);
		compra.setDec_valor_consulta(new BigDecimal(valorConsulta.getValor()));
		
		// Checa se o usuario esta logado
		Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
		if (usuarioLogado == null)
			return Response.status(Status.UNAUTHORIZED).entity("Não").build();
		UsuarioDTO beneficiario = compra.getUsuario();
		beneficiario.setId_usuario(usuarioLogado.getId_usuario());
		compra.setUsuario(beneficiario);

		if (!compra.checkCamposSemPayment()) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Campos obrigatórios para a compra não foram preeenchidos").build();
		}
		
		ServicoDTO agendamento = new ServicoDTO(compra);
		Compra compraPaga = null;
		try {
			compraPaga = compraDAO.findByVoucher(compra.getLong_voucher(), usuarioLogado.getId_usuario());
		} catch (Exception e1) {
			e1.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Ops, não foi possível verificar o seu voucher! Tente novamente mais tarde")
					.build();
		}
		
		if(compraPaga != null && compraPaga.getId_compra() != null) {
			if(compraPaga.getDec_valor_consulta().compareTo(compra.getDec_valor_consulta()) == 0){
				try {
					agendamentoConfirmado = agendaHorario(agendamento);
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(Status.BAD_REQUEST)
							.entity("Ops, alguém chegou primeiro. Esse horário não está mais disponível, agende outro horário!")
							.build();
				}
				
				if (agendamentoConfirmado == null) {
					return Response.status(Status.BAD_REQUEST)
							.entity("Ops, alguém chegou primeiro. Esse horário não está mais disponível, agende outro horário!")
							.build();
				}
				
				Compra compraAtualizada = null;
				try {
					ServicoDAO agendaDAO =  new ServicoDAO();
					compraPaga.setStr_especialidade(compra.getStr_especialidade());
					compraPaga.setStr_matricula_credenciado(compra.getStr_matricula_credenciado());
					compraPaga.setStr_matricula_prestador(compra.getStr_matricula_prestador());
					compraPaga.setDate_consulta(compra.getDate_consulta());
					compraPaga.setStr_associado_endereco(compra.getStr_associado_endereco());
					compraPaga.setStr_nome_associado(compra.getStr_nome_associado());
					Servico servicoSalvo = agendaDAO.findOne(agendamentoConfirmado.getIdAgendamento());
					compraPaga.setServico(servicoSalvo);
					compraPaga.setDate_registro(LocalDateTime.now());
					
					compraAtualizada = compraDAO.update(compraPaga);
	
					agendamentoConfirmado.setNomeBeneficiario(compra.getUsuario().getStr_nome());
					agendamentoConfirmado.setEmailBeneficiario(compra.getUsuario().getStr_email());
					agendamentoConfirmado.setInicio(compraAtualizada.getDate_consulta().toString());
					agendamentoConfirmado.setDataCompra(compraAtualizada.getDate_registro().toString());
					agendamentoConfirmado.setNumeroVoucher(compraAtualizada.getLong_voucher().toString());
					agendamentoConfirmado.setValor(compraAtualizada.getDec_valor_consulta().toString());
	
					EmailUtil.enviarEmail(agendamentoConfirmado, "", TipoEmailEnum.AGENDAMENTO_CLIENTE);
					EmailUtil.enviarEmail(agendamentoConfirmado, "", TipoEmailEnum.AGENDAMENTO_ASSOCIADO);
					
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Seu agendamento foi realizado com sucesso, "
									+ "mas não foi possível enviar o email. Acompanhe seus agendamentos na página MEUS AGENDAMENTOS")
							.build();
				}
				compraAtualizada.setLong_voucher(null);//não mandar o voucher pro usuário ainda, seria uma falha de segurança
				return Response.ok().entity(compraAtualizada).build();
			}
			else {
				return Response.status(Status.BAD_REQUEST)
						.entity("Ops, essa compra possui um valor diferente do voucher já pago!")
						.build();
			}
		}
		else {
			return Response.status(Status.BAD_REQUEST)
					.entity("Ops, esse voucher não é válido!")
					.build();
		}
	}

	private PaymentDTO preparaCompraPagSeguro(PaymentDTO p, Usuario u, BigDecimal valorConsulta) {
		PaymentDTO payment = new PaymentDTO(p);
		UsuarioDAO dao = new UsuarioDAO();
		try {
			u = dao.find(u.getId_usuario());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//seta o nedereco com os dados a serem verificados TODO
		BillingAddressDTO billing = new BillingAddressDTO();
		billing.setCity("a");
		billing.setNumber("0");
		billing.setDistrict("a");
		billing.setState("DF");
		billing.setComplement("a");
		billing.setPostalCode(p.getCreditCard().getBillingAddress().getPostalCode());
		billing.setCountry("Brasil");
		billing.setStreet(p.getCreditCard().getBillingAddress().getStreet());

		// seta documentos do holder e tira a mascara
		String cpf = p.getCreditCard().getHolder().getDocument().get(0).getValue();
		DocumentDTO documentHolder = new DocumentDTO();
		documentHolder.setType("CPF");
		documentHolder.setValue(FormatadorCPF.retiraPontuacao(cpf));
		List<DocumentDTO> documentsHolder = new ArrayList<>();
		documentsHolder.add(documentHolder);
		
		/*PhoneDTO phone = new PhoneDTO();
		phone.setAreaCode("61");
		phone.setNumber("984526975");

		HolderDTO holder = new HolderDTO();
		holder.setName("Ana Maria");
		holder.setBirthDate("17/05/1990");
		holder.setDocument((List<DocumentDTO> ) documents);
		holder.setPhone(phone);*/

		InstallmentDTO installment = new InstallmentDTO();
		installment.setQuantity("1");
		installment.setNoInterestInstallmentQuantity("2");
		installment.setValue(valorConsulta.setScale(2).toString());

		/*CreditCardDTO card = new CreditCardDTO();
		card.setToken("7187336a82c24bff8794dc5a04a79f20");
		card.setBillingAddress(billing);
		card.setHolder(holder);/
		card.setInstallment(installment);*/
		
		p.getCreditCard().setBillingAddress(billing);
		p.getCreditCard().getHolder().setDocument(documentsHolder);
		p.getCreditCard().setInstallment(installment);

		//TODO substituir por dados do usuario
		PhoneDTO phone = new PhoneDTO();
		phone.setAreaCode(u.getStr_telefone().substring(1, 3)); // pega o ddd
		phone.setNumber(u.getStr_telefone().substring(4).replaceAll("\\D", "")); // pega o resto e retira tudo que nao é numero

		DocumentDTO document = new DocumentDTO();
		document.setType("CPF");
		String cpfComprador = FormatadorCPF.retiraPontuacao(u.getStr_cpf());
		document.setValue(cpfComprador);
		List<DocumentDTO> documents = new ArrayList<>();
		documents.add(document);

		// Pega os dados do usuario
		SenderDTO sender = new SenderDTO();
		sender.setHash(p.getSender().getHash());
		sender.setPhone(phone);
		sender.setDocument(documents);
		//sender.setEmail(u.getStr_email());
		sender.setEmail("anamaria@sandbox.pagseguro.com.br");
		sender.setName(u.getStr_nome());

		//TODO caso exista carrinho é isso que vai mudar
		ItemDTO item = new ItemDTO();
		item.setId("1");
		item.setDescription("Consulta");
		item.setAmount(valorConsulta.setScale(2).toString());
		item.setQuantity("1");
		List<ItemDTO> items = new ArrayList<>();
		items.add(item);

		//TODO substituir por dados do usuario 
		AddressDTO address = new AddressDTO();
		address.setCity("0");
		address.setComplement("0");
		address.setCountry("0");
		address.setDistrict("0");
		address.setNumber("0");
		address.setPostalCode(u.getStr_cep());
		address.setState("DF");
		address.setStreet(u.getStr_endereco());

		ShippingDTO shipping = new ShippingDTO();
		shipping.setAddress(address);
		shipping.setCost("0.00");
		shipping.setType("3");
		
		payment.setCurrency("BRL");
		payment.setMethod("creditCard");
		payment.setMode("default");
		payment.setItem(items);
		payment.setSender(sender);
		payment.setCreditCard(p.getCreditCard());
		payment.setShippingAddressRequired("false");
		payment.setShipping(shipping);
		return payment;
	}

	@POST
	@Path("/private/checarBoleto")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response checarBoleto(final CompraDTO compra) {

		return Response.ok().build();
	}

	@GET
	@Path("/private/registradas")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response listaRegistradas() {
		// Lista todas as compras registradas mas ainda não pagas
		CompraDAO dao = new CompraDAO();

		try {
			List<Compra> comprasRegistradas = dao.getComprasRegistradas();
			return Response.ok().entity(new GenericEntity<List<Compra>>(comprasRegistradas) {
			}).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar as compras no banco de dados")
					.build();
		}

	}

	private Compra registraCompra(final CompraDTO compra, final Integer idAgendamento) throws Exception {
		// Quando o cliente finaliza a compra e insere os dados
		CompraDAO dao = new CompraDAO();
		Compra compraRegistrada = new Compra(compra, idAgendamento);
		try {
			compraRegistrada.setLong_voucher(Voucher.gerar());
			compraRegistrada.setDate_registro(LocalDateTime.now());
			return dao.save(compraRegistrada);
		} catch (Exception e) {
			throw new Exception(e);
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
			servico = dao.save(servico);

			agendamentoConfirmado.setIdAgendamento(servico.getId_servico());
			agendamentoConfirmado.setDataCompra(hoje.format(formatter).toString());
			//TODO trocar para o email do associado em prod
			agendamentoConfirmado.setPrestadorEmail("cliquemedicos@amhp.com.br");//AMHP
			return agendamentoConfirmado;

		} else {
			String output = responseAMHP.readEntity(String.class);
			System.out.println(output);
			return null;
		}
	}

	private Response cancelaHorario(Integer idAmhp, Integer idAgendamento) {
		Response responseAMHP = client.target(AmbienteUtil.AHMP_REST_URI).path("/AgendaConsulta/" + idAmhp)
				.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("api-key", AmbienteUtil.AHMP_API_KEY)
				.delete();

		if (responseAMHP.getStatus() == 200) {
			SuperDAO<Servico, Serializable> dao = new SuperDAO<>(Servico.class);
			try {
				dao.delete(idAgendamento);
			} catch (Exception e) {
				e.printStackTrace();
				return Response.serverError().entity("Erro ao excluir agendamento").build();
			}
			return Response.ok().build();
		}

		return responseAMHP;
	}
}
