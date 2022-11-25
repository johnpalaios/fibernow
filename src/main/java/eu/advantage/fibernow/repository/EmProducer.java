package eu.advantage.fibernow.repository;

import jakarta.enterprise.inject.Produces;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class  EmProducer {

    private static final String FIBERNOW_PU = "Fibernow";

    @PersistenceContext(unitName = FIBERNOW_PU)
    private EntityManager em;

    @Produces
    public EntityManager getEntityManager() {
        return em;
    }
}

