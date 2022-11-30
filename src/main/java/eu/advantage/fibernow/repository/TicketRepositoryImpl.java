package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Ticket;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.advantage.fibernow.util.JPAHelper.getEntityManager;

public class TicketRepositoryImpl extends AbstractRepository<Ticket, Long> implements ITicketRepository {
    public TicketRepositoryImpl() {
        this.setPersistentClass(Ticket.class);
    }

    @Override
    public List<Ticket> findTicketsBetweenDates(LocalDate startDate, LocalDate endDate) {
        EntityManager em = getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
        Root<Ticket> root = criteriaQuery.from(Ticket.class);

        Predicate date =  builder.between(root.get("receivedDate"), root.<LocalDate>get("startDate"), root.<LocalDate>get("endDate"));
        criteriaQuery.select(root).where(date);

        TypedQuery<Ticket> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);

        return typedQuery.getResultList();
    }
}
