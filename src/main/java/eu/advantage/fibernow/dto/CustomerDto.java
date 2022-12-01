package eu.advantage.fibernow.dto;

import eu.advantage.fibernow.model.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String tin;
    private String name;
    private String surname;
    private String address;
    private Set<String> phoneNumber;
    private Set<String> email;
    private String username;
    private String password;
    private Status status;
    private Set<TicketDto> tickets;
}
