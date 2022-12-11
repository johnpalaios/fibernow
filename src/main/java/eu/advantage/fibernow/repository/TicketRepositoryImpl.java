package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Ticket;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class TicketRepositoryImpl extends AbstractRepository<Ticket, Long> implements TicketRepository {

    @Inject
    EntityManager em;

    public TicketRepositoryImpl() {
        this.setPersistentClass(Ticket.class);
    }

    @Override
    public List<Ticket> findTicketsBetweenDates(LocalDate startDate, LocalDate endDate) {
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
