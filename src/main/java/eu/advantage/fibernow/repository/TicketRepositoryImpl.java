package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Customer;
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

        criteriaQuery.select(root).where(builder.between(root.get("receivedDate"), startDate, endDate));

        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Ticket> findTicketsByCustomer(Customer customer) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
        Root<Ticket> root = criteriaQuery.from(Ticket.class);
        criteriaQuery.select(root).where(builder.equal(root.get("customer"), customer));
        return em.createQuery(criteriaQuery).getResultList();
    }
}
