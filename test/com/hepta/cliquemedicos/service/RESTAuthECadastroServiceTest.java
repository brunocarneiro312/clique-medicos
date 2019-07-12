package com.hepta.cliquemedicos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hepta.cliquemedicos.dto.UsuarioDTO;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.persistence.UsuarioDAO;


class RESTAuthECadastroServiceTest {
	
	private static HttpServletRequest  request;
	private static RESTAuthService     serviceAuth;
	private static RESTCadastroService serviceCadastro;
	private static HttpSession         session;
	
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	private static final int INTERNAL_SERVER_ERROR = 500;
	private static final int NO_CONTENT            = 204;
	private static final int HTTP_OK               = 200;
	
	@BeforeAll
	static void beforeClass() throws Exception {		
		
		// ARRANGE
		request = Mockito.mock(HttpServletRequest.class);
		session = Mockito.mock(HttpSession.class);
		
		Mockito.when(request.getSession()).thenReturn(session);
		
		serviceAuth = new RESTAuthService();
		serviceAuth.setRequest(request);

		serviceCadastro = new RESTCadastroService();
		serviceCadastro.setRequest(request);
	}
	
	/**
	 * 
	 * Testa o cadastro de um usuário com sucesso.
	 * 
	 */
	private Response givenUsuarioOK_whenCadastrar_thenReturn200() {
		
		// given
		UsuarioDTO usuarioDeTestes = this.getUsuarioDTO();
		
		// when
		Response response = serviceCadastro.cadastrar(usuarioDeTestes);
		
		// then
		assertEquals(HTTP_OK, response.getStatusInfo().getStatusCode());
		
		return response;
	}
	
	/**
	 * Testa a recuperação de um usuário pelo seu email
	 */
	private Usuario givenEmailUsuario_whenBuscar_thenReturn200() throws Exception {
		
		// given
		UsuarioDTO usuarioDTO = this.getUsuarioDTO();
		
		// when
		Usuario usuario = this.usuarioDAO.buscaUsuariosPorEmail(usuarioDTO.getStr_email()).get(0);
		
		// then
		assertNotNull(usuario);
		
		return usuario;
	}
	
	/**
	 * Testa o mecanismo de login da aplicação
	 */
	private Response givenUsuarioOK_whenLogar_thenReturn200(Response response) {
		
		// given
		UsuarioDTO usuarioDeTeste = this.getUsuarioDTO();
		
		// when
		response = this.serviceAuth.login(usuarioDeTeste);
		
		// then
		assertEquals(HTTP_OK, response.getStatusInfo().getStatusCode());
		
		return response;
	}
	
	/**
	 * Testa a validação do usuário pela token de verificação
	 */
	private Response givenToken_whenVerificar_thenExpectUsuarioVerificado(String token) {
		
		// when
		Response response = this.serviceCadastro.verificar(token);
		
		// then
		assertEquals(HTTP_OK, response.getStatusInfo().getStatusCode());
		
		return response;
	}
	
	/**
	 * Testa a exclusão do usuário
	 */
	private void givenUsuario_whenDeletar_thenExpectUsuarioDeletado(Integer idUsuario) throws Exception {
		
		// when
		Boolean isUsuarioRemoved = this.usuarioDAO.delete(idUsuario);
		
		// then
		assertTrue(isUsuarioRemoved);
		
	}
	
	@Test
	public void testSuite() throws Exception {
		
		Response response = null;
		
		/**
		 * 
		 * CADASTRO
		 * 
		 * Dado um usuário válido,
		 * Quando este usuário é cadastrado,
		 * Então espera-se que o cadastro ocorra com sucesso
		 */
		response = givenUsuarioOK_whenCadastrar_thenReturn200();
		
		/**
		 * 
		 * BUSCA
		 * 
		 * Dado um ID de usuário,
		 * Quando este usuário é buscado pelo ID,
		 * Então espera-se que o usuário seja encontrado
		 */
		Usuario usuario = givenEmailUsuario_whenBuscar_thenReturn200();
		
		/**
		 * 
		 * LOGIN
		 * 
		 * Dado um usuário válido,
		 * Quando este usuário efetuar o login da aplicação,
		 * Então espera-se que o login funcione corretamente.
		 */
		response = givenUsuarioOK_whenLogar_thenReturn200(response);
		
		/**
		 * 
		 * TOKEN
		 * 
		 * Dado um token válido,
		 * Quando o método de validar token for executado,
		 * Então espera-se que a token esteja verificada
		 */
		response = givenToken_whenVerificar_thenExpectUsuarioVerificado(usuario.getStr_token_verificacao());
		
		/**
		 * 
		 * ALTERAR
		 * 
		 * Dado um usuário,
		 * Quando o método de alteração for executado,
		 * Então espera-se que o usuário seja alterado
		 */
		givenUsuarioOK_whenAlterar_then200();
		
		/**
		 * 
		 * DELETAR
		 * 
		 * Dado um usuário,
		 * Quando o usuário for deletado,
		 * Então espera-se que o usuário seja removido da base de dados
		 */
		givenUsuario_whenDeletar_thenExpectUsuarioDeletado(usuario.getId_usuario());
		
	}
	
	/**
	 * Dado um usuário,
	 * Quando o usuário for alterado,
	 * Então retorna status 200
	 *  
	 */
	private void givenUsuarioOK_whenAlterar_then200() throws Exception {
		
		// Test fixture
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		
		Usuario usuarioMock = new Usuario();
		usuarioMock.setStr_nome    ("Bruno Antes Do Teste");
		usuarioMock.setStr_cep     ("70765110");
		usuarioMock.setStr_endereco("SQN 312 K 102");
		usuarioMock.setStr_telefone("(61) 77777-7777");
		usuarioMock.setStr_email   ("teste@teste.com");
		usuarioMock.setStr_senha   ("senhateste");
		
		usuarioDAO.save(usuarioMock);
		Usuario ret = usuarioDAO.buscaUsuariosPorEmail(usuarioMock.getStr_email()).get(0);
		
		// given
		UsuarioDTO usuarioASerAlterado = new UsuarioDTO();
		usuarioASerAlterado.setId_usuario  (ret.getId_usuario());
		usuarioASerAlterado.setStr_nome    ("Bruno Depois Do Teste");
		usuarioASerAlterado.setStr_cep     ("70765110");
		usuarioASerAlterado.setStr_endereco("SQN 312 K 102");
		usuarioASerAlterado.setStr_telefone("(61) 88888-8888");
		usuarioASerAlterado.setStr_email   ("teste@teste.com");
		usuarioASerAlterado.setStr_senha   ("senhatesteok");
		
		// when
		Response response = serviceCadastro.alterar(usuarioASerAlterado);
		usuarioDAO.delete(usuarioMock.getId_usuario());
		
		// then
		assertEquals(HTTP_OK,                 response.getStatus());
		assertEquals("Bruno Depois Do Teste", ((UsuarioDTO)response.getEntity()).getStr_nome());
		assertEquals("70765110",              ((UsuarioDTO)response.getEntity()).getStr_cep());
		assertEquals("SQN 312 K 102",         ((UsuarioDTO)response.getEntity()).getStr_endereco());
		assertEquals("(61) 88888-8888",       ((UsuarioDTO)response.getEntity()).getStr_telefone());
		assertEquals("teste@teste.com",       ((UsuarioDTO)response.getEntity()).getStr_email());
		assertEquals("senhatesteok",          ((UsuarioDTO)response.getEntity()).getStr_senha());
	}
	
	/**
	 * Dado um usuário nulo,
	 * quando o usuário for alterado,
	 * Então retorna status 204
	 */
	@Test
	public void givenUsuarioNull_whenAlterar_then204() {
		
		// given
		UsuarioDTO usuario = null;
		
		// when
		Response response = serviceCadastro.alterar(usuario);
		
		// then
		assertEquals(NO_CONTENT, response.getStatus());
		assertEquals("Não foi possível recuperar o usuário.", response.getEntity());
	}
	
	/**
	 * 
	 * Obtém um usuário mock
	 * 
	 * @return
	 * 		UsuarioDTO (mock)
	 */
	private UsuarioDTO getUsuarioDTO() {
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		
		usuarioDTO.setId_usuario           (null);
		usuarioDTO.setStr_cep              ("71591270");
		usuarioDTO.setStr_cpf              ("82194286195");
		usuarioDTO.setStr_email            ("davi.diniz@hepta.com.br");
		usuarioDTO.setStr_endereco         ("Quadra 24");
		usuarioDTO.setStr_nome             ("Diogo Gabriel Martins");
		usuarioDTO.setStr_senha            ("teste");
		usuarioDTO.setStr_telefone         ("61994286195");
		usuarioDTO.setData_registro        (null);
		usuarioDTO.setBool_verificado      (false);
		usuarioDTO.setStr_token_verificacao(null);
		usuarioDTO.setData_token           (null);
		usuarioDTO.setData_nascimento      (LocalDateTime.now());
		
		return usuarioDTO;
	}

}
