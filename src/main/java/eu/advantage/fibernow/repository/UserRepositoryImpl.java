package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.UserCredentials;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Stateless
public class UserRepositoryImpl implements UserRepository {
    @Inject
    EntityManager em;

    @Override
    public UserCredentials getCredentialsByUsername(String username) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserCredentials> criteriaQuery = builder.createQuery(UserCredentials.class);
        Root<UserCredentials> root = criteriaQuery.from(UserCredentials.class);
        criteriaQuery.select(root).where(builder.equal(root.get("username"), username));
        return em.createQuery(criteriaQuery).getSingleResult();
    }
}
