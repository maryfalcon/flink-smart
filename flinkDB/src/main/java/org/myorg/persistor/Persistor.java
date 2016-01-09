package org.myorg.persistor;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.myorg.model.Datahash;





public class Persistor {

	protected static final String ENTITY_MANAGER_FACTORY_NAME = "org.myorg.flinkDB_flinkDB_war_0.1PU";
	protected EntityManagerFactory factory;

	public Persistor() {
		factory = Persistence.createEntityManagerFactory(ENTITY_MANAGER_FACTORY_NAME);
	}
        
        
        public Datahash insertDatahash(Datahash dto){
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
        
        public Datahash getDatahashByFileId(int fileid){
            EntityManager entityManager = null;
        List<Datahash> cheques = new ArrayList<Datahash>();
        Query query = null;
        try {
            entityManager = factory.createEntityManager();
            query = entityManager.createNamedQuery("Datahash.findByFlinkdbid");
            query.setParameter("flinkdbid", fileid);
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
        
        public Datahash updateDatahash(Datahash dto){
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = factory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(dto);
            transaction.commit();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return dto;
    }
        
}
