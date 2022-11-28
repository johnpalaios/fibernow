package eu.advantage.fibernow.repository;

import java.util.List;
import java.util.Map;

public interface IGenericRepository<T, K> {
    void create(T obj);
    T findById(K id);
    List<T> findAll();
    List<T> findByCriteria(Map<String, Object> criteria);
    <G extends T> List<G> findByCriteria(Class<G> clazz, Map<String, Object> criteria);
    void update(T obj);
    void delete(T obj);
}
