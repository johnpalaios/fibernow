package eu.advantage.fibernow.model;

import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.TicketType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "id", nullable = false)
    private Customer customer;
    private LocalDate receivedDate; // the date the ticket was received
    private LocalDateTime scheduledDatetime; // date and time of the scheduled action
    private TicketStatus status; // default : STANDBY
    private TicketType type;
    private BigDecimal estimatedCost;
    private String address;
    private String description;
}
