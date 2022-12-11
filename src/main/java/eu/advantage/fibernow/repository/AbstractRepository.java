package eu.advantage.fibernow.repository;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.List;

public class AbstractRepository<T, K> implements GenericRepository<T, K> {
    private Class<T> persistentClass;

    @Inject
    private EntityManager em;

    protected AbstractRepository() {}

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public void create(T obj) {
        em.persist(obj);
    }

    @Override
    public T findById(K id) {
        return em.find(persistentClass, id);
    }

    @Override
    public List<T> findAll() {
        return searchByCriteria(getPersistentClass(), null, null);
    }

    public List<T> searchByCriteria(String key, String value) {
        return searchByCriteria(getPersistentClass(), key, value);
    }

    public <G extends T> List<G> searchByCriteria(Class<G> clazz, String key, String value) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<G> criteriaQuery = builder.createQuery(clazz);
        Root<G> root = criteriaQuery.from(clazz);

        if (key == null) {
            criteriaQuery.select(root);
            return em.createQuery(criteriaQuery).getResultList();
        }

        criteriaQuery.select(root).where(builder.like(root.get(key), value.concat("%")));
        TypedQuery<G> typedQuery = em.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    @Override
    public void update(T obj) {
        em.merge(obj);
    }

    @Override
    public void delete(T obj) {
        em.remove(obj);
    }
}
