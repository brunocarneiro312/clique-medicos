package com.hepta.cliquemedicos.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.hepta.cliquemedicos.dto.UsuarioDTO;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.security.Encryptor;
import com.hepta.cliquemedicos.util.TokenVerificacao;
import com.hepta.cliquemedicos.util.ValidadorCPF;

public class UsuarioDAO extends SuperDAO<Usuario, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	public UsuarioDAO() {
		super(Usuario.class);
	}

	public Usuario autenticar(UsuarioDTO dto) throws NoResultException, Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Usuario usuario = null;
		if ((dto.getStr_cpf() == null || dto.getStr_cpf().isEmpty())
				&& (dto.getStr_email() == null || dto.getStr_email().isEmpty()))
			return null;

		if (!ValidadorCPF.validar(dto.getStr_cpf()))
			return null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT c ");
			sql.append(" FROM Usuario c ");
			if (dto.getStr_cpf().isEmpty())
				sql.append(" WHERE c.str_email = :str_email ");
			else
				sql.append(" WHERE c.str_cpf = :str_cpf ");

			Query query = em.createQuery(sql.toString());
			if (dto.getStr_cpf().isEmpty())
				query.setParameter("str_email", dto.getStr_email());
			else
				query.setParameter("str_cpf", dto.getStr_cpf());

			usuario = (Usuario) query.getSingleResult();

			if (Encryptor.compare(dto.getStr_senha(), usuario.getStr_senha())) {
				usuario.setStr_senha("");
				return usuario;
			} else
				return null;
		} catch (NoResultException e) {
			em.getTransaction().rollback();
			throw new NoResultException();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
	}

	public Usuario cadastrar(UsuarioDTO usuario) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Usuario novo = new Usuario(usuario);
		novo.setStr_senha(usuario.getStr_senha());
		if (!novo.checarCampos())
			return null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT c ");
			sql.append(" FROM Usuario c ");
			sql.append(" WHERE c.str_cpf =:str_cpf OR c.str_email =:str_email");

			Query query = em.createQuery(sql.toString());
			query.setParameter("str_cpf", usuario.getStr_cpf());
			query.setParameter("str_email", usuario.getStr_email());

			if (query.getResultList().isEmpty()) {
				novo.setStr_token_verificacao(TokenVerificacao.generate());
				novo.setData_token(LocalDateTime.now());
				novo.setData_registro(LocalDateTime.now());
				novo.setBool_verificado(false);
				novo.setStr_senha(Encryptor.encrypt(novo.getStr_senha()));
				return save(novo);
			} else {
				return null;
			}
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
	}

	public Integer updateVerificado(Integer id, Boolean tokenVerificado) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Integer result = 0;
		try {
			em.getTransaction().begin();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE Usuario u ");
			sql.append(" SET bool_verificado=:token ");
			sql.append(" WHERE u.id_usuario=:id_usuario ");
	
			Query query = em.createQuery(sql.toString());
			query.setParameter("token", tokenVerificado);
			query.setParameter("id_usuario", id);
			result = query.executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		
		return result;

	}

	// @SuppressWarnings("unchecked")
	// public List<Usuario> buscaUsuariosPorNome(String nom) throws Exception {
	// EntityManager em = HibernateUtil.getEntityManager();
	// List<Usuario> list = new ArrayList<>();
	//
	// try {
	// StringBuilder sql = new StringBuilder();
	// sql.append(" FROM Usuario ");
	// if (nom != null && !nom.isEmpty())
	// sql.append(" WHERE str_nome LIKE CONCAT('%', :nom, '%') ");
	// sql.append(" ORDER BY str_nome ");
	//
	// Query query = em.createQuery(sql.toString());
	//
	// if (nom != null && !nom.isEmpty())
	// query.setParameter("nom", nom);
	//
	// list = query.getResultList();
	// } catch (Exception e) {
	// em.getTransaction().rollback();
	// throw new Exception(e);
	// } finally {
	// em.close();
	// }
	// return list;
	// }
	//
	// @SuppressWarnings("unchecked")
	// public List<Usuario> buscaUsuariosPorCPF(String str_cpf) throws Exception {
	// EntityManager em = HibernateUtil.getEntityManager();
	// List<Usuario> list = new ArrayList<>();
	//
	// try {
	// StringBuilder sql = new StringBuilder();
	// sql.append(" FROM Usuario ");
	// if (str_cpf != null && !str_cpf.isEmpty())
	// sql.append(" WHERE str_cpf = :str_cpf ");
	// sql.append(" ORDER BY str_cpf ");
	//
	// Query query = em.createQuery(sql.toString());
	//
	// if (str_cpf != null && !str_cpf.isEmpty())
	// query.setParameter("str_cpf", str_cpf);
	//
	// list = query.getResultList();
	// } catch (Exception e) {
	// em.getTransaction().rollback();
	// throw new Exception(e);
	// } finally {
	// em.close();
	// }
	// return list;
	// }

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaUsuariosPorEmail(String str_email) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Usuario> list = new ArrayList<>();

		if (str_email == null || str_email.isEmpty()) {
			return list;
		}

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Usuario ");
			sql.append(" WHERE str_email = :str_email ");
			sql.append(" ORDER BY str_email ");

			Query query = em.createQuery(sql.toString());
			query.setParameter("str_email", str_email);

			list = query.getResultList();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaUsuariosPorToken(String str_token_verificacao) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Usuario> list = new ArrayList<>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Usuario ");
			sql.append(" WHERE str_token_verificacao = :str_token_verificacao ");

			Query query = em.createQuery(sql.toString());
			query.setParameter("str_token_verificacao", str_token_verificacao);

			list = query.getResultList();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return list;
	}

	/**
	 * Verifica login através de <b>email</b> e <b>senha</b>
	 * 
	 * @param email
	 * 		Email do usuário
	 * @param senha
	 * 		Senha do usuário
	 * @return
	 * @throws Exception 
	 */
	public Usuario buscarPorEmailESenha(String email, String senha) throws Exception {
		
		EntityManager em = HibernateUtil.getEntityManager();
		Usuario usuario = null;
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Usuario ");
			sql.append(" WHERE str_email = :email AND str_senha = :senha ");
			
			Query query = em.createQuery(sql.toString());
			query.setParameter("email", email);
			query.setParameter("senha", senha);
			
			usuario = (Usuario) query.getSingleResult();
			return usuario;
		}
		catch(Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		}
		finally {
			em.close();
		}
		
	}
	
	/**
	 * 
	 * Verifica se o email informado consta na base de dados
	 * 
	 * @param 
	 * 		email Email a ser verificado
	 * @return 
	 * 		Usuario ao qual o email pertence
	 * @throws Exception 
	 */
	public boolean verificarEmailCadastrado(String email) throws Exception {
		
		EntityManager em = HibernateUtil.getEntityManager();
		
		// Tentando efetuar consulta
		try {

			List<Usuario> usuarios = this.buscaUsuariosPorEmail(email);
			
			if (!usuarios.isEmpty()) {
				return true;
			}
			
		}
		// Em caso de erro durante a obtenção do EntityManager, trata erro
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			em.close();
		}
		
		return false;
	}
}
