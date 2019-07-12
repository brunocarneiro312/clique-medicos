package com.hepta.cliquemedicos.persistence;

import static org.junit.jupiter.api.Assertions.fail;

import javax.persistence.NoResultException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hepta.cliquemedicos.dto.UsuarioDTO;
import com.hepta.cliquemedicos.entity.Usuario;

class UsuarioDAOTest {

	private UsuarioDTO usuarioCorreto;
	private UsuarioDTO usuarioErrado;
	private UsuarioDAO daoUsuario;

	@BeforeEach
	void setUp() throws Exception {
		daoUsuario = new UsuarioDAO();

		usuarioCorreto = new UsuarioDTO();
		usuarioCorreto.setId_usuario(null);
		usuarioCorreto.setStr_cep("71501-503");
		usuarioCorreto.setStr_cpf("090.181.774.07");
		usuarioCorreto.setStr_email("teste@gmail.com");
		usuarioCorreto.setStr_endereco("endereço teste");
		usuarioCorreto.setStr_nome("nome teste");
		usuarioCorreto.setStr_senha("testesenha");
		usuarioCorreto.setStr_telefone("(61)99999-9999");

		usuarioErrado = new UsuarioDTO();
		usuarioErrado.setId_usuario(null);
		usuarioErrado.setStr_cep("");
		usuarioErrado.setStr_cpf("");
		usuarioErrado.setStr_email("");
		usuarioErrado.setStr_endereco("");
		usuarioErrado.setStr_nome("");
		usuarioErrado.setStr_senha("");
		usuarioErrado.setStr_telefone("");
	}

	@Test
	void testUsuarioCorreto() {
		try {
			Usuario cadastrado = daoUsuario.cadastrar(usuarioCorreto);
			if (cadastrado == null)
				fail("Não conseguiu cadastrar usuário correto");
			if (daoUsuario.buscaUsuariosPorEmail(usuarioCorreto.getStr_email()).isEmpty())
				fail("Não conseguiu buscar usuário cadastrado");

			Usuario autenticado = daoUsuario.autenticar(usuarioCorreto);

			if (autenticado == null)
				fail("Não conseguiu autenticar usuário cadastrado");
			if (!daoUsuario.delete(autenticado.getId_usuario()))
				fail("Não conseguiu deletar usuário cadastrado");
			if (!daoUsuario.buscaUsuariosPorEmail(usuarioCorreto.getStr_email()).isEmpty())
				fail("Não deletou usuário cadastrado, pois deu pra achar ele na database");
		} catch (NoResultException e) {
			e.printStackTrace();
			fail("Erro: ver stacktrace, teste com usuário correto");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro: ver stacktrace, teste com usuário correto");
		}
	}

	@Test
	void testUsuarioErrado() {
		try {
			Usuario cadastrado = daoUsuario.cadastrar(usuarioErrado);
			if (cadastrado != null)
				fail("Conseguiu cadastrar usuário errado");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro: ver stacktrace, teste com usuário errado");
		}
		try {
			if (!daoUsuario.buscaUsuariosPorEmail(usuarioErrado.getStr_email()).isEmpty())
				fail("Conseguiu buscar usuário não cadastrado");
		} catch (NoResultException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro: ver stacktrace, teste com usuário errado");
		}
		try {
			Usuario autenticado = daoUsuario.autenticar(usuarioErrado);

			if (autenticado != null)
				fail("Conseguiu autenticar usuário não cadastrado");
			if (daoUsuario.delete(usuarioErrado.getId_usuario()))
				fail("Conseguiu deletar usuário não cadastrado");
		} catch (NoResultException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro: ver stacktrace, teste com usuário errado");
		}
		try {
			if (!daoUsuario.buscaUsuariosPorEmail(usuarioErrado.getStr_email()).isEmpty())
				fail("Deletou usuário não cadastrado, pois deu pra achar ele na database");
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro: ver stacktrace, teste com usuário errado");
		}
	}

}
