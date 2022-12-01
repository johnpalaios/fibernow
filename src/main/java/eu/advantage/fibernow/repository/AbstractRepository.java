package eu.advantage.fibernow.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static eu.advantage.fibernow.util.JPAHelper.*;

public abstract class AbstractRepository<T, K> implements IGenericRepository<T, K>{
    private Class<T> persistentClass;

    protected AbstractRepository() {}

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public void create(T obj) {
        getEntityManager().persist(obj);
    }

    @Override
    public T findById(K id) {
        return getEntityManager().find(persistentClass, id);
    }

    @Override
    public List<T> findAll() {
        return findByCriteria(getPersistentClass(), Collections.<String, Object>emptyMap());
    }

    @Override
    public List<T> findByCriteria(Map<String, Object> criteria) {
        return findByCriteria(getPersistentClass(), criteria);
    }

    @Override
    public <G extends T> List<G> findByCriteria(Class<G> clazz, Map<String, Object> criteria) {
        EntityManager em = getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<G> criteriaQuery = builder.createQuery(clazz);
        Root<G> root = criteriaQuery.from(clazz);

        List<Predicate> predicates = getPredicateList(builder, root, criteria);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<G> typedQuery = em.createQuery(criteriaQuery);
        setParametersToQuery(typedQuery, criteria);

        return typedQuery.getResultList();
    }



    @Override
    public void update(T obj) {
        getEntityManager().merge(obj);
    }

    @Override
    public void delete(T obj) {
        getEntityManager().remove(obj);
    }

    private List<Predicate> getPredicateList(CriteriaBuilder builder, Root<? extends T> entityRoot, Map<String, Object> parameters) {
        List<Predicate> predicateList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String inputString = entry.getKey();
            Object value = entry.getValue();
            ParameterExpression<?> param = builder.parameter(value.getClass(), createParameterIdentifier(inputString));
            Predicate equal = builder.equal(createPath(entityRoot, inputString), param);
            predicateList.add(equal);
        }
        return predicateList;
    }

    private String createParameterIdentifier(String inputStringPath) {
        return inputStringPath.replaceAll("\\.", "");
    }

    private Path<?> createPath(Root<? extends T> root, String expression) {
        String[] fields = expression.split("\\.");
        Path<?> path = root.get(fields[0]);
        for (String field: fields) {
            path = path.get(field);
        }
        return path;
    }

    private <G extends T> void setParametersToQuery(TypedQuery<G> query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            query.setParameter(createParameterIdentifier(entry.getKey()), value);
        }
    }
}
