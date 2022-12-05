package eu.advantage.fibernow.dto;

import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.TicketType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class TicketDto  implements Serializable {
    private Long id;
    private CustomerDto customerDto;
    private LocalDate receivedDate; // the date the ticket was received
    private LocalDateTime scheduledDatetime; // date and time of the scheduled action
    private TicketStatus status; // default : STANDBY
    private TicketType type;
    private BigDecimal estimatedCost;
    private String address;
    private String description;
}
