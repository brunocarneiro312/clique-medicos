package com.hepta.cliquemedicos.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.hepta.cliquemedicos.dto.MensagemBoletoDTO;
import com.hepta.cliquemedicos.dto.enums.EstadoBoletoEnum;
import com.hepta.cliquemedicos.dto.enums.EstadoCompraEnum;
import com.hepta.cliquemedicos.dto.pagseguro.TransactionDTO;
import com.hepta.cliquemedicos.entity.Boleto;
import com.hepta.cliquemedicos.entity.Compra;

public class CompraDAO extends SuperDAO<Compra, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	public CompraDAO() {
		super(Compra.class);
	}

	@SuppressWarnings("unchecked")
	public List<Compra> getComprasRegistradas() throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Compra> result = new ArrayList<Compra>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Compra c ");
			sql.append(" JOIN FETCH c.servico ");
			sql.append(" JOIN FETCH c.boleto ");
			sql.append(" WHERE c.long_voucher = null");

			Query query = em.createQuery(sql.toString());
			result = (List<Compra>) query.getResultList();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Compra> getHistorico(Integer id) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Compra> result = new ArrayList<Compra>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Compra c ");
			sql.append(" JOIN FETCH c.servico ");
			sql.append(" JOIN FETCH c.boleto ");
			sql.append(" WHERE c.date_consulta < CURRENT_DATE() and c.usuario.id_usuario =  :id");

			Query query = em.createQuery(sql.toString());
			query.setParameter("id", id);
			result = (List<Compra>) query.getResultList();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Compra> getProximos(Integer id) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Compra> result = new ArrayList<Compra>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Compra c ");
			sql.append(" JOIN FETCH c.servico ");
			sql.append(" WHERE c.date_consulta >= CURRENT_DATE() AND c.usuario.id_usuario =  :id");

			Query query = em.createQuery(sql.toString());
			query.setParameter("id", id);
			result = (List<Compra>) query.getResultList();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Compra> getNaoPagos() throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Compra> result = new ArrayList<Compra>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Compra c ");
			sql.append(" JOIN FETCH c.servico ");
			sql.append(" WHERE c.int_estado_compra = :estado1 ");
			sql.append(" OR c.int_estado_compra = :estado2 ");

			Query query = em.createQuery(sql.toString());
			query.setParameter("estado1", EstadoCompraEnum.AGUARDANDO_PAGAMENTO.getValor());
			query.setParameter("estado2", EstadoCompraEnum.EM_ANALISE.getValor());
			result = (List<Compra>) query.getResultList();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}

		return result;
	}
	// Compra do Boleto
	@SuppressWarnings("unchecked")
	public List<Compra> getBoletosAguardandoPagamento() throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Compra> result = new ArrayList<Compra>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Compra c ");
			sql.append(" JOIN FETCH c.boleto b");// traz apenas os item que possuem boleto (inner join)
			sql.append(" JOIN FETCH c.servico a");
			sql.append(" JOIN FETCH c.usuario u"); //FIXME como trazer apenas o id? (andressa.valadares)
			sql.append(" WHERE c.int_estado_compra = :estado ");
			//sql.append(" AND c.str_transaction_code = null "); // apenas dados que nao sao de cartao

			Query query = em.createQuery(sql.toString());
			query.setParameter("estado", EstadoCompraEnum.AGUARDANDO_PAGAMENTO.getValor());
			result = (List<Compra>) query.getResultList();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}

		return result;
	}
	
	public Integer atualizaEstadoBoleto(EstadoCompraEnum novo, List<Integer> boletosAtualizados) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		
		Integer result = 0;
		if(boletosAtualizados.isEmpty())
			return result;
		
		try {	
			em.getTransaction().begin();
			
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE Compra c ");
			sql.append(" SET  c.int_estado_compra = :estadonovo");
			sql.append(" WHERE c.boleto.id_boleto in ( SELECT b.id_boleto FROM Boleto b WHERE b.id_amhp in (:idsList))");
	
			Query query = em.createQuery(sql.toString());
			query.setParameter("estadonovo", novo.getValor());
			query.setParameter("idsList", boletosAtualizados);
			
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
	
	public Compra findByVoucher(Long voucher, Integer idUsuario) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		
		Compra result = null;
		if(idUsuario == null || voucher == null)
			return result;
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Compra c ");
			sql.append(" WHERE c.long_voucher = :voucher");
			sql.append(" AND c.usuario.id_usuario = :id AND c.servico.id_servico = null");
	
			Query query = em.createQuery(sql.toString());
			query.setParameter("voucher", voucher);
			query.setParameter("id", idUsuario);
			
			result = (Compra) query.getSingleResult();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return result;
	}
	//TODO altera nome (andressa.valadares)
	public Integer atualizaEstado(EstadoCompraEnum novo, List<TransactionDTO> transactions) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		
		Integer result =  0;
		if(transactions.isEmpty())
			return result;
		
		try {
			em.getTransaction().begin();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE Compra c ");
			sql.append(" SET int_estado_compra =:estadonovo ");
			for (int i = 0; i < transactions.size(); i++) {
				if (i == 0)
					sql.append(" WHERE c.str_transaction_code = :code" + i + " ");
				else
					sql.append(" OR c.str_transaction_code = :code" + i + " ");
			}
			
			Query query = em.createQuery(sql.toString());
			query.setParameter("estadonovo", novo.getValor());
			for (int i = 0; i < transactions.size(); i++) {
				query.setParameter("code" + i, transactions.get(i).getCode());
			}
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

	@SuppressWarnings("unchecked")
	public List<Compra> getServicoECompras(List<TransactionDTO> transacoes) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();

		List<Compra> result = new ArrayList<>();
		if(transacoes.isEmpty())
			return result;

		try {
			StringBuilder sql1 = new StringBuilder();
			sql1.append(" SELECT c,a FROM Compra c ");
			sql1.append(" join fetch c.servico a ");
			for (int i = 0; i < transacoes.size(); i++) {
				if (i == 0)
					sql1.append(" WHERE c.str_transaction_code = :code" + i + " ");
				else
					sql1.append(" OR c.str_transaction_code = :code" + i + " ");
			}

			Query query1 = em.createQuery(sql1.toString());
			for (int i = 0; i < transacoes.size(); i++) {
				query1.setParameter("code" + i, transacoes.get(i).getCode());
			}
			result = (List<Compra>) query1.getResultList();

		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}

		return result;
	}

}
