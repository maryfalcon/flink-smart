package org.myorg.persistor;

import org.myorg.model.Data;
import org.myorg.model.User;

import javax.persistence.*;
import java.util.List;
public class Persistor {

    protected static final String ENTITY_MANAGER_FACTORY_NAME = "org.myorg.quickstart_quickstart_jar_0.1PU";
    protected EntityManagerFactory factory;

    public Persistor() {
        factory = Persistence.createEntityManagerFactory(ENTITY_MANAGER_FACTORY_NAME);
    }

    public Data insertData(Data dto) {
        EntityManager entityManager = null;
        EntityTransaction transaction;
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

    public Data getDataByName(String name) {
        EntityManager entityManager = null;
        List<Data> cheques;
        Query query;
        try {
            entityManager = factory.createEntityManager();
            query = entityManager.createNamedQuery("Data.findByName");
            query.setParameter("name", name);
            cheques = query.getResultList();
            if (cheques.size() > 0) {
                return cheques.get(0);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return null;
    }

    public User insertUser(User dto) {
        EntityManager entityManager = null;
        EntityTransaction transaction;
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
    
    public User checkUserByHash(String hash){
        EntityManager entityManager = null;
        List<User> cheques;
        Query query;
        try {
            entityManager = factory.createEntityManager();
            query = entityManager.createNamedQuery("User.findByHash");
            query.setParameter("lh", hash.getBytes());
            cheques = query.getResultList();
            if (cheques.size() > 0) {
                return cheques.get(0);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return null;
    }

    public Data getDataById(int id) {
        EntityManager entityManager = null;
        List<Data> cheques;
        Query query ;
        try {
            entityManager = factory.createEntityManager();
            query = entityManager.createNamedQuery("Data.findById");
            query.setParameter("id", id);
            cheques = query.getResultList();
            if (cheques.size() > 0) {
                return cheques.get(0);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return null;
    }

    public List<Data> getAllData() {
        EntityManager entityManager = null;
        List<Data> cheques;
        Query query;
        try {
            entityManager = factory.createEntityManager();
            query = entityManager.createNamedQuery("Data.findAll");
            cheques = query.getResultList();
            if (cheques.size() > 0) {
                return cheques;
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return null;
    }
}
