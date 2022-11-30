package eu.advantage.fibernow.model;

import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.TicketType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "id", nullable = false)
    private Customer customer;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "receivedDate")
    private LocalDate receivedDate; // the date the ticket was received
    @Column(name = "scheduledDatetime")
    private LocalDateTime scheduledDatetime; // date and time of the scheduled action
    @Column(name = "status")
    private TicketStatus status; // default : STANDBY
    @Column(name = "type")
    private TicketType type;
    @Column(name = "estimatedCost")
    private BigDecimal estimatedCost;
    @Column(name = "address")
    private String address;
    @Column(name = "description")
    private String description;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Ticket ticket = (Ticket) o;
        return id != null && Objects.equals(id, ticket.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
