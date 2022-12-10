package eu.advantage.fibernow.repository;

import java.util.List;
import java.util.Map;

public interface GenericRepository<T, K> {
    void create(T obj);
    T findById(K id);
    List<T> findAll();
    List<T> searchByCriteria(String key, String value);
    <G extends T> List<G> searchByCriteria(Class<G> clazz, String key, String value);
    void update(T obj);
    void delete(T obj);
}
