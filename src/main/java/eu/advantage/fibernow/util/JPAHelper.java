package eu.advantage.fibernow.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JPAHelper {

    private static EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    private JPAHelper(){}

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("fibernowPU");
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();
        if (em == null || !em.isOpen()) {
            em = getEntityManagerFactory().createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        if (getEntityManager() != null) {
            getEntityManager().close();
        }
    }

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    public static void commitTransaction() {
        getEntityManager().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        getEntityManager().getTransaction().rollback();
    }

    public static void closeEntityMangerFactory() {
        emf.close();
    }
}
