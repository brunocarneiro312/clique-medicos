package com.hepta.cliquemedicos.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.hepta.cliquemedicos.util.AmbienteUtil;

public class HibernateUtil {
	private static EntityManagerFactory factory;

	public static EntityManagerFactory getEntityManagerFactory() throws Exception {
		if (factory == null) {
			createEntityManagerFactory();
		}
		return factory;
	}

	public static void shutdown() {
		if (factory != null) {
			factory.close();
		}
	}

	public static void createEntityManagerFactory() throws Exception{
		factory = Persistence.createEntityManagerFactory(AmbienteUtil.PERSISTENCE_UNIT_NAME);
	}

	public static EntityManager getEntityManager() throws Exception {
		return HibernateUtil.getEntityManagerFactory().createEntityManager();
	}

	public static void main(String[] args) {
		try {
			System.out.println(HibernateUtil.getEntityManagerFactory().createEntityManager());
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateUtil.shutdown();
	}
}