package eu.advantage.fibernow.dto;


import eu.advantage.fibernow.model.Status;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AdminDto {
    private Long id;
    private String name;
    private String surname;
    private Set<String> email;
    private Set<String> phoneNumber;
    private String username;
    private String password;
    private Status status;
}
