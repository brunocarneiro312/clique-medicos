package com.hepta.cliquemedicos.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.hepta.cliquemedicos.dto.MensagemBoletoDTO;
import com.hepta.cliquemedicos.dto.enums.EstadoBoletoEnum;
import com.hepta.cliquemedicos.dto.enums.EstadoCompraEnum;
import com.hepta.cliquemedicos.entity.Boleto;

public class BoletoDAO extends SuperDAO<Boleto, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	public BoletoDAO() {
		super(Boleto.class);
	}
	

	
	public Integer atualizaEstado(EstadoBoletoEnum novo, List<MensagemBoletoDTO> boletosAtualizados) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Integer result = 0;
		if(boletosAtualizados.isEmpty())
			return result;
		try {
			em.getTransaction().begin();
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE Boleto b ");
			sql.append(" SET  b.str_status = :estadonovo, date_modificacao= :hoje"); // TODO atualizar a descricao do status (andressa.valadares)
			for (int i = 0; i < boletosAtualizados.size(); i++) {
				if (i == 0)
					sql.append(" WHERE b.id_amhp = :id" + i + " ");
				else
					sql.append(" OR b.id_amhp = :id" + i + " ");
			}
	
			Query query = em.createQuery(sql.toString());
			query.setParameter("hoje", new Date());
			query.setParameter("estadonovo", novo.getValor());
			for (int i = 0; i < boletosAtualizados.size(); i++) {
				query.setParameter("id" + i, boletosAtualizados.get(i).getIdAMHP());
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
}
