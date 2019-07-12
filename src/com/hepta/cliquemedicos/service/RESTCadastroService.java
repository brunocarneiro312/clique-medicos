package com.hepta.cliquemedicos.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.cliquemedicos.dto.CamposEmailDTO;
import com.hepta.cliquemedicos.dto.NovaSenhaDTO;
import com.hepta.cliquemedicos.dto.UsuarioDTO;
import com.hepta.cliquemedicos.dto.enums.TipoEmailEnum;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.persistence.UsuarioDAO;
import com.hepta.cliquemedicos.security.Encryptor;
import com.hepta.cliquemedicos.util.EmailUtil;
import com.hepta.cliquemedicos.util.TokenVerificacao;
import com.hepta.cliquemedicos.util.ValidadorCPF;

@Path("cadastro")
public class RESTCadastroService {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@GET
	@Path("/private/deletar")
	public Response deletar() {
		try {
			Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
			if (usuarioLogado == null)
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();

			UsuarioDAO daoUsuario = new UsuarioDAO();

			Usuario usu = daoUsuario.find(usuarioLogado.getId_usuario());
			if (usu == null)
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity("Erro ao deletar usuário, usuário inexistente").build();
			if (usu.getId_usuario().equals(usuarioLogado.getId_usuario())) {
				daoUsuario.delete(usuarioLogado.getId_usuario());
				return Response.ok().entity("Usuário deletado com sucesso").build();
			} else {
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao deletar usuário").build();
		}
	}

	@POST
	@Path("/private/deletar")
	public Response checkDeletar(String senha) throws Exception {
		
			
			// Obtém usuário logado
			Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
			
			// Acesso não autorizado caso não esteja logado
			if (usuarioLogado == null) {				
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();
			}

			// Verifica se usuário existe na base
			UsuarioDAO daoUsuario = new UsuarioDAO();
			Usuario usu = daoUsuario.find(usuarioLogado.getId_usuario());
			
			// Se não existir, lança erro
			if (usu == null) {				
				return Response
						.status(Status.INTERNAL_SERVER_ERROR)
						.entity("Erro ao deletar usuário, usuário inexistente")
						.build();
			}
			// Se existir...
			if (usu.getId_usuario().equals(usuarioLogado.getId_usuario())) {
				
				// Se o usuário for validado...
				if (Encryptor.compare(senha, usu.getStr_senha())) {
					
					// tenta efetuar exclusão
					daoUsuario.delete(usuarioLogado.getId_usuario());
					
					// Volta mensagem de sucesso
					return Response.ok().entity("Usuário deletado com sucesso").build();
				}
				// Se a senha não estiver correta, volta mensagem de erro.
				else {
					return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Credenciais inválidas.").build();
				}
				
			}
		 
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao deletar usuário").build();

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/private/buscar")
	public Response buscar() {
		try {
			Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
			if (usuarioLogado == null)
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();

			UsuarioDAO daoUsuario = new UsuarioDAO();

			UsuarioDTO usuario = new UsuarioDTO(daoUsuario.find(usuarioLogado.getId_usuario()));
			usuario.setStr_senha("");

			if (usuario.getId_usuario().equals(usuarioLogado.getId_usuario())) {
				GenericEntity<UsuarioDTO> entity = new GenericEntity<UsuarioDTO>(usuario) {
				};
				return Response.ok().entity(entity).build();
			} else {
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar usuario").build();
		}
	}
	
	/**
	 * Altera um usuário
	 * 
	 * @param id
	 * 		Id do usuário a ser alterado
	 * @param usuarioDTO
	 * 		Dados de alteração do usuário
	 * @return
	 * 		Usuario alterado
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/private/alterar")
	public Response alterar(UsuarioDTO usuarioDTO) {
	
		try {
			
			// Iniciando alteração do usuário
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			
			// O usuário deve ser informado no request
			if (usuarioDTO != null) {
				
				// Busca o usuário a ser alterado
				Usuario usuario = usuarioDAO.find(usuarioDTO.getId_usuario());
				
				// Se houver um usuário retornado...
				if (usuario != null) {
					
					
					// Se o e-mail for alterado,
					// devemos alterar o email para não-verificado (bool_verificado = 0)
					// e disparar automaticamente um e-mail de validação para o novo email
					if (!usuario.getStr_email().equalsIgnoreCase(usuarioDTO.getStr_email())) {
						
						usuario.setBool_verificado(false);
						
						CamposEmailDTO campos = new CamposEmailDTO();
						campos.setEmailBeneficiario(usuarioDTO.getStr_email());
						
						EmailUtil.enviarEmail(campos, usuarioDTO.getStr_token_verificacao(), TipoEmailEnum.VERIFICACAO);
						
					}
					
					// Seta alterações
					usuario.setStr_nome       (usuarioDTO.getStr_nome());
					usuario.setStr_email      (usuarioDTO.getStr_email());
					usuario.setStr_cpf        (usuarioDTO.getStr_cpf());
					usuario.setStr_telefone   (usuarioDTO.getStr_telefone());
					usuario.setStr_cep        (usuarioDTO.getStr_cep());
					usuario.setStr_endereco   (usuarioDTO.getStr_endereco());
					usuario.setData_nascimento(usuarioDTO.getData_nascimento());
					usuario.setStr_logradouro (usuarioDTO.getStr_logradouro());
					usuario.setStr_complemento(usuarioDTO.getStr_complemento());
					usuario.setStr_bairro	  (usuarioDTO.getStr_bairro());
					usuario.setStr_localidade (usuarioDTO.getStr_localidade());
					usuario.setStr_uf		  (usuarioDTO.getStr_uf());
					usuario.setStr_unidade	  (usuarioDTO.getStr_unidade());

					
					// Verifica se houve alteração de senha
					if (usuarioDTO.getStr_senha() != null && !usuarioDTO.getStr_senha().isEmpty()) {
						usuario.setStr_senha(Encryptor.encrypt(usuarioDTO.getStr_senha()));
					}
					
					// Atualiza
					usuarioDAO.update(usuario);
					
					// Retorno 200
					GenericEntity<UsuarioDTO> entity = new GenericEntity<UsuarioDTO>(usuarioDTO) {};
					return Response
							.status(Status.OK)
							.entity(entity)
							.build();
					
				}
			
			}
			
		}
		catch (Exception e) {
			
			// Caso haja algum erro, retorna status 500 com mensagem
			e.printStackTrace();
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Erro ao alterar usuário.")
					.build();
		}
		
		// Caso não seja retornado nenhum usuário, retorna status 204 com mensagem
		return Response
				.status(Status.NO_CONTENT)
				.entity("Não foi possível recuperar o usuário.")
				.build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/private/buscarNome")
	public Response buscarNome() {
		try {
			Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
			if (usuarioLogado == null)
				return Response.status(Status.UNAUTHORIZED).entity(null).build();

			UsuarioDAO daoUsuario = new UsuarioDAO();

			Usuario usuario = daoUsuario.find(usuarioLogado.getId_usuario());
			Usuario somenteNome = new Usuario();
			somenteNome.setStr_nome(usuario.getStr_nome());
			return Response.ok().entity(somenteNome).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar usuario").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/cadastrar")
	public Response cadastrar(UsuarioDTO usuario) {
		try {
			if (usuario.getId_usuario() != null && usuario.getId_usuario() < 0)
				usuario.setId_usuario(null);

			UsuarioDAO daoUsuario = new UsuarioDAO();

			if (!ValidadorCPF.validar(usuario.getStr_cpf()))
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("CPF inválido.").build();
			
			Usuario cadastrado = daoUsuario.cadastrar(usuario);
			if (cadastrado != null) {
				CamposEmailDTO campos = new CamposEmailDTO();
				campos.setEmailBeneficiario(cadastrado.getStr_email());
				try {
					EmailUtil.enviarEmail(campos, cadastrado.getStr_token_verificacao(),TipoEmailEnum.VERIFICACAO);
				} catch(Exception e) {
					daoUsuario.delete(cadastrado.getId_usuario());
					throw new Exception(e);
				}
				return Response.ok().entity("Usuário cadastrado com sucesso").build();
			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Usuário com esse login ou nome já existe")
						.build();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Não foi possível finalizar o cadastro. Verifique se seu e-mail está correto.").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/private/editar")
	public Response editar(UsuarioDTO editado) {
		try {
			Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("user");
			if (usuarioLogado == null)
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();

			if (editado.getId_usuario() != null && editado.getId_usuario() == -1)
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Usuário inválido, erro ao editar usuário")
						.build();

			UsuarioDAO daoUsuario = new UsuarioDAO();
			Usuario atual = daoUsuario.find(editado.getId_usuario());

			if (atual.getId_usuario().equals(usuarioLogado.getId_usuario())) {

				//ver se já existe alguém com esse e-mail novo
				if (!editado.getStr_email().equals(atual.getStr_email())) {
					if (daoUsuario.buscaUsuariosPorEmail(editado.getStr_email()).isEmpty())
						atual.setStr_email(editado.getStr_email());
					else
						return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Usuário com esse e-mail já existe")
								.build();
				}

				atual.setStr_cep(editado.getStr_cep());
				atual.setStr_endereco(editado.getStr_endereco());
				atual.setStr_nome(editado.getStr_nome());
				atual.setStr_senha(Encryptor.encrypt(editado.getStr_senha()));
				atual.setStr_telefone(editado.getStr_telefone());
				atual.setData_nascimento(editado.getData_nascimento());
				daoUsuario.update(atual);
				return Response.status(Status.OK).entity("Usuário editado com sucesso").build();
			} else {
				return Response.status(Status.UNAUTHORIZED).entity("Não").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao editar usuario").build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/verificar")
	public Response verificar(@QueryParam("token") String token) {
		try {
			UsuarioDAO daoUsuario = new UsuarioDAO();
			List<Usuario> res = daoUsuario.buscaUsuariosPorToken(token);
			if (res.isEmpty())
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Página expirada.").build();

			Usuario usuario = new Usuario(res.get(0));
			if (usuario.getBool_verificado()) {
				response.sendRedirect("../../verificar.html?estado=2");
				return Response.status(Status.UNAUTHORIZED).entity("Página expirada.").build();
			}else {
				Long tempoEntreDatas = usuario.getData_token().until(LocalDateTime.now(), ChronoUnit.HOURS);
				if (tempoEntreDatas > 24) {//token expirado?
					CamposEmailDTO campos = new CamposEmailDTO();
					campos.setEmailBeneficiario(usuario.getStr_email());
					EmailUtil.enviarEmail(campos, token,TipoEmailEnum.VERIFICACAO);
					//EmailUtil.enviarEmailVerificacaoHTMLAmazon(campos, token);
					response.sendRedirect("../../verificar.html?estado=1");
					return Response.status(Status.UNAUTHORIZED).entity("Token expirado, novo e-mail enviado.").build();
				} else {
					if (usuario.getStr_token_verificacao().equals(token)) {
						
						daoUsuario.updateVerificado(usuario.getId_usuario(), true);
						
						if (response != null) {							
							response.sendRedirect("../../verificar.html?estado=0");
						}
						
						return Response.ok().entity("Conta verificada com sucesso.").build();
						
					} else {
						return Response.status(Status.UNAUTHORIZED).entity("Página expirada.").build();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Página expirada.").build();
		}
	}
	
	/**
	 * 
	 * Endpoint para reenvio de e-mail de validação
	 * 
	 * @param usuario
	 * 		Usuário para qual o e-mail será enviado
	 * 
	 * @return
	 * 		status 200
	 * 		status 500 
	 */
	@POST
	@Path("/enviar-email")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response enviarEmail(UsuarioDTO usuario) {
		try {
			CamposEmailDTO emailDTO = new CamposEmailDTO();
			emailDTO.setEmailBeneficiario(usuario.getStr_email());
			EmailUtil.enviarEmail(emailDTO, usuario.getStr_token_verificacao(), TipoEmailEnum.VERIFICACAO);
			return Response.status(Status.OK).entity("E-mail enviado com sucesso!").build();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Não foi possível enviar o E-mail.").build();
	}
	
	/**
	 * Método de abstração de envio de email.
	 * Uso interno.
	 * 
	 * @param campos
	 * @param token
	 * @throws Exception
	 */
	public void enviarEmailVerificacao(CamposEmailDTO campos, String token) throws Exception {
		// EmailUtil.enviarEmailAmazon(campos, token, TipoEmailEnum.VERIFICACAO);
		EmailUtil.enviarEmail(campos, token, TipoEmailEnum.VERIFICACAO);
	}
	
	/**
	 * 
	 * Realiza o envio de email de recuperação de senha para o usuário logado
	 * 
	 * @param usuarioLogado
	 * 		Usuário lodago no sistema
	 * @param email
	 * 		E-mail a qual o link de redefinição de senha será enviado
	 * @return
	 * 		Response.ok() em caso de 200
	 * 		Response.serverError() em caso de 500
	 * @throws Exception
	 */
	@Path("/enviar-email-recuperacao")
	@POST
	public Response enviarEmailDeRecuperacao(String email) throws Exception {

		String responseMessage = "Verifique no e-mail o link para redenifir a senha.";
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		
		try {
			
			// Caso o email exista, manda email de verificação
			if (usuarioDAO.verificarEmailCadastrado(email)) {
				
				CamposEmailDTO camposEmail = new CamposEmailDTO();
				camposEmail.setEmailBeneficiario(email);
				
				Usuario usuario = usuarioDAO.buscaUsuariosPorEmail(email).get(0); 
				usuario.setStr_token_verificacao(TokenVerificacao.generate());
				usuario.setData_token(LocalDateTime.now());
				
				// Update na token de verificação
				usuarioDAO.update(usuario);
				
				// Enviando email para alteração de senha
				EmailUtil.enviarEmail(
						camposEmail, 
						usuario.getStr_token_verificacao(), 
						TipoEmailEnum.ALTERAR_SENHA);
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok().entity(responseMessage).build();
	}
	
	@GET
	@Path("/enviar-email-recuperacao/confirmar")
	public Response verificarLinkRecuperacaoSenha(@QueryParam("token") String token) throws Exception {
		
		UsuarioDAO    usuarioDAO      = new UsuarioDAO();
		List<Usuario> usuarios        = usuarioDAO.buscaUsuariosPorToken(token);
		Usuario       usuario         = usuarios.get(0);
		Long          tempoEntreDatas = usuario.getData_token().until(LocalDateTime.now(), ChronoUnit.MINUTES);
		
		final Integer TEMPO_MAXIMO = 30;
		
		// Se o token estiver expirado
		if (tempoEntreDatas > TEMPO_MAXIMO) {
			System.out.println("Token expirada.");
		} 
		else {
			
			if (usuario.getStr_token_verificacao().equals(token)) {
				
				// Redireciona usuário para página de recuperação de senha
				response.sendRedirect("../../../esqueceuSenha.html?token=" + token);
				
				return Response.status(Status.OK).entity("OK.").build();
			} 
			else {
				
				return Response.status(Status.UNAUTHORIZED).entity("Página expirada.").build();
			}
		}
		
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Token expirada.").build();
	}
	
	@PUT
	@Path("/nova-senha")
	public Response criarNovaSenha(NovaSenhaDTO novaSenhaDTO) {
		
		String cryptoNovaSenha = Encryptor.encrypt(novaSenhaDTO.getNovaSenha());
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = null;
		
		try {
			usuario = usuarioDAO.buscaUsuariosPorToken(novaSenhaDTO.getToken()).get(0);
			usuario = usuarioDAO.find(usuario.getId_usuario()); // attach

			// Só cria nova senha caso a token não tenha expirado
			if (usuario.getData_token().until(LocalDateTime.now(), ChronoUnit.MINUTES) < 30) {
				
				// Altero a senha do usuário
				usuario.setStr_senha(cryptoNovaSenha);
				usuarioDAO.update(usuario);
				return Response.ok().entity("Senha cadastrada com sucesso").build();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.serverError().entity("Erro ao criar nova senha").build();
	}
}
