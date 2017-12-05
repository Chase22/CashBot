package org.chase.telegram.cashbot.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityUtility {
	public final static EntityManagerFactory factory;
	static {
		factory = Persistence.createEntityManagerFactory("cashbot"); //$NON-NLS-1$
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
}
