package eu.advantage.fibernow.model;


import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class Admin {
    private Long id;
    private String name;
    private String surname;
    private Set<String> email;
    private Set<String> phoneNumber;
    private String username;
    private String password;
    private Status status;
}
