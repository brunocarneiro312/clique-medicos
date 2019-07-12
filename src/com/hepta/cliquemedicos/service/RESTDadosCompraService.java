package com.hepta.cliquemedicos.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.cliquemedicos.dto.AssociadosDTO;
import com.hepta.cliquemedicos.dto.CompraDTO;
import com.hepta.cliquemedicos.dto.EspecialidadeDTO;
import com.hepta.cliquemedicos.dto.enums.EstadoCompraEnum;
import com.hepta.cliquemedicos.entity.Compra;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.persistence.CompraDAO;
import com.hepta.cliquemedicos.util.AmbienteUtil;

@Path("compras")
public class RESTDadosCompraService {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	private Client client = ClientBuilder.newClient();

	@GET
	@Path("/private/historico")
	public Response historico() {
		try {
			Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
			if (usuarioLogado == null)
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();
			CompraDAO dao = new CompraDAO();
			List<Compra> agendamentosComprados = dao.getHistorico(usuarioLogado.getId_usuario());
			List<CompraDTO> resultado = new ArrayList<CompraDTO>();
			// TODO otimizar isso ai
			for (Compra a : agendamentosComprados) {
				a.setBoleto(null); //FIXME isso eh um gato para os casos com boleto, assim não ocorre um LAZYException
				CompraDTO c = new CompraDTO(a);
				AssociadosDTO associado = getAssociadoAMHP(c.getStr_especialidade(), c.getStr_matricula_credenciado(), 
						c.getStr_matricula_prestador(),  c.getStr_associado_endereco());
				EspecialidadeDTO especialidade = getEspecialidadeAMHP(c.getStr_especialidade());
				
				c.setStr_associado_endereco(associado.getEndereco() +", "+ associado.getBairrodesc());
				c.setStr_especialidade(especialidade.getDescricao());
				c.setStr_nome_associado(associado.getNomePrestador());
				
				if(c.getInt_estado_compra().equals(EstadoCompraEnum.AGUARDANDO_PAGAMENTO.getValor())
				|| c.getInt_estado_compra().equals(EstadoCompraEnum.CANCELADA.getValor())
				|| c.getInt_estado_compra().equals(EstadoCompraEnum.EM_ANALISE.getValor()))
					c.setLong_voucher(null);
				resultado.add(c);
			}
			resultado.sort(Comparator.comparing(CompraDTO::getDate_registro).reversed());
			GenericEntity<List<CompraDTO>> entity = new GenericEntity<List<CompraDTO>>(resultado) {
			};
			return Response.ok().entity(entity).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar histórico de agendamentos.")
					.build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/private/agendamentos")
	public Response agendamentos() {
		try {
			Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
			if (usuarioLogado == null)
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();

			//TODO pesquisa dos dados do agendamento na api do amhp
			CompraDAO dao = new CompraDAO();
			List<Compra> agendamentosComprados = dao.getProximos(usuarioLogado.getId_usuario());
			List<CompraDTO> resultado = new ArrayList<CompraDTO>();
			
			for (Compra compra : agendamentosComprados) {
				compra.setBoleto(null); //FIXME isso eh um gato para os casos com boleto, assim não ocorre um LAZYException
				CompraDTO compraDTO = new CompraDTO(compra);
				
				AssociadosDTO associado = getAssociadoAMHP(
						compraDTO.getStr_especialidade(), 
						compraDTO.getStr_matricula_credenciado(), 
						compraDTO.getStr_matricula_prestador(),  
						compraDTO.getStr_associado_endereco());
				
				EspecialidadeDTO especialidade = getEspecialidadeAMHP(compraDTO.getStr_especialidade());
				
				compraDTO.setStr_associado_endereco(associado.getEndereco() +", "+ associado.getBairrodesc());
				compraDTO.setStr_especialidade(especialidade.getDescricao());
				compraDTO.setStr_nome_associado(associado.getNomePrestador());
				
				boolean isCompraAguardandoPagamento = compraDTO.getInt_estado_compra().equals(EstadoCompraEnum.AGUARDANDO_PAGAMENTO.getValor()); 
				boolean	isCompraCancelada           = compraDTO.getInt_estado_compra().equals(EstadoCompraEnum.CANCELADA.getValor());
				boolean isCompraEmAnalise           = compraDTO.getInt_estado_compra().equals(EstadoCompraEnum.EM_ANALISE.getValor()); 
				
				// Se a compra estiver em um dos três estados, então seta voucher para null
				if (isCompraAguardandoPagamento || isCompraCancelada || isCompraEmAnalise) {					
					compraDTO.setLong_voucher(null);
				}
				
				resultado.add(compraDTO);
			}
			
			resultado.sort(Comparator.comparing(CompraDTO::getDate_consulta).reversed());
			GenericEntity<List<CompraDTO>> entity = new GenericEntity<List<CompraDTO>>(resultado) {
			};
			return Response.ok().entity(entity).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar agendamentos.").build();
		}
	}
	
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