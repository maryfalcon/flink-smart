package org.myorg.persistor;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.myorg.model.Data;




public class Persistor {

	protected static final String ENTITY_MANAGER_FACTORY_NAME = "org.myorg.quickstart_quickstart_jar_0.1PU";
	protected EntityManagerFactory factory;

	public Persistor() {
		factory = Persistence.createEntityManagerFactory(ENTITY_MANAGER_FACTORY_NAME);
	}
        
        
        public Data insertData(Data dto){
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = factory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(dto);
            transaction.commit();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return dto;
    }
        
        public Data getDataByName(String name){
            EntityManager entityManager = null;
        List<Data> cheques = new ArrayList<Data>();
        Query query = null;
        try {
            entityManager = factory.createEntityManager();
            query = entityManager.createNamedQuery("Data.findByName");
            query.setParameter("name", name);
            cheques = query.getResultList();
            if(cheques.size()>0)
                return cheques.get(0);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return null;
        }
        
}
