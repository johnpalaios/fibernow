package eu.advantage.fibernow.dto;

import eu.advantage.fibernow.model.Status;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CustomerDto {
    private Long id;
    private String tin;
    private String firstname;
    private String lastname;
    private String address;
    private Set<String> phoneNumber;
    private Set<String> email;
    private String username;
    private String password;
    private Status status;
}
