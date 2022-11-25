package eu.advantage.fibernow.repository;

import jakarta.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class GenericRepository <T, K> {
    @Inject
    private EntityManager em;

    public void create(T obj) {
        em.persist(obj);
    }

    public void update(T obj) {
        em.merge(obj);
    }

    public abstract T findById(K id);

    public void delete(T obj) {
        em.remove(obj);
    }

    public abstract List<T> findAll();

    protected EntityManager getEm() {
        return em;
    }

    public abstract TypedQuery<T> createQuery(String query);
}
