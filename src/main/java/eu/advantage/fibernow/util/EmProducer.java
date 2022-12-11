package eu.advantage.fibernow.util;

import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class EmProducer {

    private static final String FIBER_NOW_PU = "FiberNow-PU";

    @PersistenceContext(unitName = FIBER_NOW_PU)
    private EntityManager em;

    @Produces
    public EntityManager getEntityManager() {
        return em;
    }
}
